package com.chowziy.androidtools

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chowziy.androidtools.databinding.ActivityFadeEdgeDemoBinding
import com.chowziy.atvs.quickmenu.QuickPopupMenu

class FadeEdgeDemoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFadeEdgeDemoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.longPressMenuView.apply {
            setMenuItems(
                listOf(
                    QuickPopupMenu.MenuItem(
                        title = "菜单项1",
                        iconResId = R.drawable.ic_launcher_foreground,
                        onClick = {
                            Toast.makeText(context, "点击了菜单项1", Toast.LENGTH_SHORT).show()
                        }
                    ),
                    QuickPopupMenu.MenuItem(
                        title = "菜单项2",
                        iconResId = R.drawable.ic_launcher_foreground,
                        onClick = {
                            Toast.makeText(context, "点击了菜单项2", Toast.LENGTH_SHORT).show()
                        }
                    ),
                    QuickPopupMenu.MenuItem(
                        title = "菜单项3",
                        iconResId = R.drawable.ic_launcher_foreground,
                        onClick = {
                            Toast.makeText(context, "点击了菜单项3", Toast.LENGTH_SHORT).show()
                        }
                    )
                )
            )


        }


    }
}
