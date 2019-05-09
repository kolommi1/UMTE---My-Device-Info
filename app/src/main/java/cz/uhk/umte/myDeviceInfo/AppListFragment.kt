package cz.uhk.umte.myDeviceInfo

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_app_list.*
import android.content.pm.PackageInfo
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import android.text.Spannable
import android.text.SpannableStringBuilder



class AppListFragment : Fragment() {

    var originalList = mutableListOf<PackageInfo>()
    var appList = mutableListOf<PackageInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pm = activity!!.packageManager
        appList = pm.getInstalledPackages(0)
        originalList = pm.getInstalledPackages(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = Adapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        packageCheckBox.setOnCheckedChangeListener { _, isChecked ->
            adapter.notifyDataSetChanged()
        }
        systemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterApps()
            adapter.notifyDataSetChanged()
        }
        userCheckBox.setOnCheckedChangeListener { _, isChecked ->
            filterApps()
            adapter.notifyDataSetChanged()
        }

        userCheckBox.isChecked = true
    }


    private fun categoryToString(category: Int): String {
        var temp = "Kategorie: "
        when(category){
            ApplicationInfo.CATEGORY_AUDIO -> temp += "Audio"
            ApplicationInfo.CATEGORY_GAME -> temp += "Hry"
            ApplicationInfo.CATEGORY_IMAGE -> temp += "Obrázky"
            ApplicationInfo.CATEGORY_MAPS -> temp += "Mapy"
            ApplicationInfo.CATEGORY_NEWS -> temp += "Novinky"
            ApplicationInfo.CATEGORY_PRODUCTIVITY -> temp += "Produktivita"
            ApplicationInfo.CATEGORY_SOCIAL -> temp += "Social"
            ApplicationInfo.CATEGORY_VIDEO -> temp += "Video"
            else -> return ""
        }
        return temp
    }

    private fun filterApps() {
        val tempList = mutableListOf<PackageInfo>()
        for(app in originalList){
            // System application
            if (app.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                if(systemCheckBox.isChecked){
                    tempList.add(app)
                }
            }
            // Application installed as update to system application
            else if (app.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                if(systemCheckBox.isChecked){
                    tempList.add(app)
                }
            }
            // User application
            else {
                if(userCheckBox.isChecked){
                    tempList.add(app)
                }
            }
        }
        appList = tempList
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.Holder>(){

        override fun onCreateViewHolder(root: ViewGroup, p1: Int): Holder {
            return Holder(LayoutInflater.from(context).inflate(R.layout.row_app, root, false))
        }

        override fun onBindViewHolder(holder: Holder, p0: Int) {
            holder.onBind()
        }

        override fun getItemCount(): Int {
            return appList.size
        }

        inner class Holder(val item: View) : RecyclerView.ViewHolder(item) {

            fun onBind(){
                val textView = item.findViewById<TextView>(R.id.nameTextView)
                val imageView = item.findViewById<ImageView>(R.id.appImageView)

                val app = appList[layoutPosition]
                val appName = app.applicationInfo.loadLabel( activity!!.packageManager)
                val icon: Drawable = app.applicationInfo.loadIcon(activity!!.packageManager)

                // display application category on newer phones
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val categoryView = item.findViewById<TextView>(R.id.categoryTextView)
                    categoryView.text =categoryToString(app.applicationInfo.category)
                }

                imageView.setImageDrawable(icon)
                if(packageCheckBox.isChecked) {
                    val boldText = SpannableStringBuilder(appName.toString() + " (" +  app.packageName + ")")
                    boldText.setSpan(
                        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        0, appName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.typeface = Typeface.DEFAULT
                    textView.text = boldText
                }
                else {
                    textView.typeface = Typeface.DEFAULT_BOLD
                    textView.text = appName
                }
            }
        }
    }

}