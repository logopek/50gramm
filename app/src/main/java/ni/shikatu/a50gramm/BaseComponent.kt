package ni.shikatu.a50gramm

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

abstract class BaseComponent{
	@SuppressLint("ComposableNaming")
	@Composable
	fun create(){
		newComponent()
		Present()
	}

	abstract fun newComponent()
	@Composable
	abstract fun Present()
}