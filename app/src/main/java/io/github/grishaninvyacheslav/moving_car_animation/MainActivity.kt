package io.github.grishaninvyacheslav.moving_car_animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main).apply {
            findViewById<MovingCarView>(R.id.car).apply {
                setOnClickListener {
                    startAnimation()
                }
            }
        }
    }
}