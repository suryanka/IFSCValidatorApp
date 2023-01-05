package com.ghosh.ifscvalidatorapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ghosh.ifscvalidatorapp.databinding.ActivityMainBinding
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
     var permissionCode: Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.INTERNET),permissionCode)
        }

//        mainBinding.idRLBankDetailsButton.setOnClickListener {
//
//            var ifscText: String=mainBinding.idETIfsc.text.toString()
//
//
//            if(ifscText.isEmpty())
//            {
//                Toast.makeText(this@MainActivity,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
//            }
//            else
//            {
//                getBankInfo(ifscText);
//            }
//
//        }

        mainBinding.Button.setOnClickListener(View.OnClickListener {
            var ifscText: String=mainBinding.idETIfsc.text.toString()
            //Toast.makeText(this@MainActivity,"Checking IFSC",Toast.LENGTH_LONG).show()

            if(ifscText.isEmpty())
            {
                Toast.makeText(this@MainActivity,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
                mainBinding.idRLInfo.visibility= View.GONE
            }
            else
            {
                getBankInfo(ifscText);
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permissionCode  && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this@MainActivity,"Permission Granted.", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this@MainActivity,"Please grant the permissions.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getBankInfo(ifscText: String)
    {
        var url:String="https://ifsc.razorpay.com/"+ifscText
        val requestQueue:RequestQueue= Volley.newRequestQueue(this@MainActivity)

        var jsonObjectRequest:JsonObjectRequest= JsonObjectRequest(Request.Method.GET,url,null,
            {response->


                mainBinding.idRLInfo.visibility= View.VISIBLE

                try {
                    mainBinding.BankName.setText(response.getString("BANK"))
                    mainBinding.BranchName.setText(response.getString("BRANCH"))
                    mainBinding.AddressDetails.setText(response.getString("ADDRESS"))
                    mainBinding.MICRCode.setText(response.getString("MICR"))
                    mainBinding.CityName.setText(response.getString("CITY"))
                    mainBinding.StateName.setText(response.getString("STATE"))
                    mainBinding.Contact.setText(response.getString("CONTACT"))
                }
                catch(e:JSONException)
                {
                    e.printStackTrace()
                }
            },
            {
                Toast.makeText(this@MainActivity,"Please enter valid IFSC.", Toast.LENGTH_LONG).show()
            })

        requestQueue.add(jsonObjectRequest)
    }
}