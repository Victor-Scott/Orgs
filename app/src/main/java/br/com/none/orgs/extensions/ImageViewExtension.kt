package br.com.none.orgs.extensions

import android.widget.ImageView
import br.com.none.orgs.R
import coil.load

//fun imageLoader(context: Context) {
//    ImageLoader.Builder(context)
//        .components {
//            if (SDK_INT >= 28) {
//                add(ImageDecoderDecoder.Factory())
//            } else {
//                add(GifDecoder.Factory())
//            }
//        }
//        .build()
//}

fun ImageView.loadImage(url: String? = null) {
    load(url) {
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.place_holder)
    }
}