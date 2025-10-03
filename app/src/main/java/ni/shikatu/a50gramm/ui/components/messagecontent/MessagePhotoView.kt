package ni.shikatu.a50gramm.ui.components.messagecontent

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ni.shikatu.a50gramm.BaseComponent
import ni.shikatu.a50gramm.tdlib.Tdlib
import ni.shikatu.a50gramm.utlis.pxToDp
import ni.shikatu.a50gramm.utlis.rememberConstrainedPhotoSize
import ni.shikatu.a50gramm.utlis.toAnnotatedString
import org.drinkless.tdlib.TdApi

class MessagePhotoView(val photo: TdApi.MessagePhoto): BaseComponent(), Tdlib.FileUpdateListener {
	val photoFile = mutableStateOf<TdApi.File?>(null)
	override fun newComponent() {
		Tdlib.subscribe(this)
		checkAndRequestPhoto()
	}

	override fun onFileUpdate(update: TdApi.UpdateFile) {
		if(update.file.id == photo.photo.sizes.last().photo.id){
			if(update.file.local.isDownloadingCompleted)
				photoFile.value = update.file
		}
	}

	fun checkAndRequestPhoto() {
		val currentFile = photo.photo.sizes.last()

		if (currentFile.photo.local.isDownloadingCompleted) {
			photoFile.value = currentFile.photo
			return
		}

		Tdlib.dowloadFile(currentFile.photo.id, 32) {}
	}
	@Composable
	override fun Present() {
		val photoCollected = photoFile.value
		val size = rememberConstrainedPhotoSize(photo)
		Column {
			if(photoCollected != null){
				AsyncImage(
					model = photoCollected.local.path,
					contentDescription = null
				)
			} else {
				AsyncImage(
					model = "",
					contentDescription = null,
					modifier = Modifier.size(size.first, size.second)
				)
			}
			if(photo.caption != null){
				Text(photo.caption.toAnnotatedString())
			}
		}

	}
}