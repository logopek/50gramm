package ni.shikatu.a50gramm

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

abstract class BaseComponent: BaseModel(){
	@SuppressLint("ComposableNaming")
	@Composable
	fun create(){
		newComponent()
		Present()
	}

	open fun newComponent() {}
	@Composable
	abstract fun Present()
}