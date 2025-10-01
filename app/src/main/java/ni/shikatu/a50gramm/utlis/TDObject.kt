package ni.shikatu.a50gramm.utlis

import org.drinkless.tdlib.TdApi

fun TdApi.Object.ensureType(type: Class<*>): Boolean {
	return type.isInstance(this);
}

fun TdApi.Object.ensureType(vararg types: Class<*>): Boolean {
	for(type in types){
		if(type.isInstance(this)){
			return true;
		}
	}
	return false;
}

inline fun <reified T : TdApi.Object> TdApi.Object.ensureType(ifIs: (T) -> Unit): Boolean {
	return if (this is T) {
		ifIs(this)
		true
	} else {
		false
	}
}
