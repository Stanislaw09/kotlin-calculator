package com.example.calculator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.databinding.ActivityMainBinding
import java.lang.Math.pow
import java.util.*
import kotlin.math.round


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var stack = ArrayDeque<Double>()
    private var moveUp = false

    private var precision=2.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var p  =  intent.getStringExtra("precision");
        if (p != null) {
            precision=p.toDouble()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.actionSettings){
            val intent= Intent(this, Settings::class.java)
            startActivity(intent)
        }

        return true
    }

    fun numberAction(view: View){
        if(view is Button){
            if(view.text=="."){
                val num=binding.stack4.text.toString()

                if(!num.contains(".") && num.isNotEmpty()){
                    if(moveUp) {
                        updateStacks(false)
                        moveUp = false
                    }

                    binding.stack4.append(view.text)
                }
            } else {
                if (moveUp) {
                    updateStacks(false)
                    moveUp = false
                }

                binding.stack4.append(view.text)
            }
        }
    }

    fun customRound(num: Double): Double {
        return (round(num*pow(10.0, precision.toDouble())) / pow(10.0, precision.toDouble()))
    }


    fun operatorAction(view: View){
        if(view is Button){
            when(view.text) {
                "+" -> {
                    if(stack.size>1){
                        val x = stack.pop()
                        val y = stack.pop()
                        stack.push(x + y)
                    }
                }
                "-" -> {
                    if(stack.size>1) {
                        val x = stack.pop()
                        val y = stack.pop()
                        stack.push(y - x)
                    }
                }
                "x" -> {
                    if(stack.size>1) {
                        val x = stack.pop()
                        val y = stack.pop()
                        stack.push(x * y)
                    }
                }
                "/" -> {
                    if(stack.size>1) {
                        val x = stack.pop()
                        val y = stack.pop()
                        stack.push(y / x)
                    }
                }
                "exp" -> {
                    if(stack.size>1) {
                        val x = stack.pop()
                        val y = stack.pop()
                        stack.push(pow(y, x))
                    }
                }
                "sqrt" -> {
                    if(stack.size>0) {
                        val x = stack.pop()
                        stack.push(pow(x, 0.5))
                    }
                }
            }
        }

        updateStacks(true)
        moveUp=true
        println(stack)
    }

    private fun updateStacks(full: Boolean=false){
        binding.stack4.text=""
        binding.stack3.text=""
        binding.stack2.text=""
        binding.stack1.text=""

        if(!stack.isEmpty()){
            val s4=if(full) customRound(stack.pop())  else 0

            if(!stack.isEmpty()){
                val s3=customRound(stack.pop())

                if(!stack.isEmpty()){
                    val s2=customRound(stack.pop())

                    if(!stack.isEmpty()){
                        val s1=customRound(stack.pop())

                        binding.stack1.text=s1.toString()
                        stack.push(s1)
                    }
                    binding.stack2.text=s2.toString()
                    stack.push(s2)
                }
                binding.stack3.text=s3.toString()
                stack.push(s3)
            }

            if(full) {
                binding.stack4.text = s4.toString()
                stack.push(s4 as Double?)
            }
        }
    }

    fun clearAction(view: View){
        binding.stack4.text=""
        stack.clear()
        updateStacks()
    }

    fun changeSign(view: View){
        if(binding.stack4.text.isNotBlank()){
            val num=binding.stack4.text.toString().toDouble()
            binding.stack4.text=(-num).toString()
        }
    }

    fun delAction(view: View){
        if(binding.stack4.text.isNotBlank()) {
            val num = binding.stack4.text.toString()
            val newNum = num.substring(0, num.length - 1)
            binding.stack4.text = newNum
        }
    }

    fun swapAction(view: View){
        if(stack.size>1){
            val x=stack.pop()
            val y=stack.pop()
            stack.push(x)
            stack.push(y)

            updateStacks()
        }
    }

    fun enterAction(view: View){
        if(binding.stack4.text.isNotBlank()){
            val num=binding.stack4.text.toString()

            stack.push(num.toDouble())

            println(stack)
            binding.stack4.text=""
            updateStacks()
        } else {
            stack.push(stack.first)
            updateStacks()
        }
    }

    fun undoAction(view: View) {}

    fun dropAction(view: View) {
        if(stack.size>0){
            stack.pop()
            updateStacks()
        }
    }
}