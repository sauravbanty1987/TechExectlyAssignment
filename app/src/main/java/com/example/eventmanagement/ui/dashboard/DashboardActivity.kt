package com.example.eventmanagement.ui.dashboard

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.activity.viewModels;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.lifecycleScope;
import androidx.recyclerview.widget.*;
import com.example.eventmanagement.R;
import com.example.eventmanagement.data.model.Event;
import com.example.eventmanagement.ui.auth.LoginActivity;
import com.example.eventmanagement.ui.event.AddEditEventActivity;
import com.example.eventmanagement.ui.event.EventViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;
import kotlinx.coroutines.launch;
import java.text.SimpleDateFormat;
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private val vm: EventViewModel by viewModels()
    private lateinit var a: EventAdapter
    private lateinit var search: EditText
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_dashboard);
        a = EventAdapter { e ->
            startActivity(
                Intent(
                    this,
                    AddEditEventActivity::class.java
                ).putExtra("id", e.id)
                    .putExtra("title", e.title)
                    .putExtra("description", e.description)
                    .putExtra("location", e.location)
                    .putExtra("date", e.dateTime?.toDate()?.time ?: 0)
            )
        };
        findViewById<RecyclerView>(R.id.recycler).layoutManager = LinearLayoutManager(this);
        findViewById<RecyclerView>(R.id.recycler).adapter = a;
        findViewById<Button>(R.id.addEvent).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddEditEventActivity::class.java
                )
            )
        }
        search = findViewById(R.id.search)
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {};
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {
                a.submit(vm.events.value, s?.toString().orEmpty())
            };
            override fun afterTextChanged(e: android.text.Editable?) {}
        })
        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this, LoginActivity::class.java)); finish()
        };
        val search = findViewById<EditText>(R.id.search);
        lifecycleScope.launch {
            vm.events.collect { list ->
                a.submit(list, search.text.toString());
                val now = Date(); findViewById<TextView>(R.id.total).text =
                "Total: ${list.size}"; findViewById<TextView>(R.id.upcoming).text = "Upcoming: ${
                list.count {
                    it.dateTime?.toDate()?.after(now) == true
                }
            }"; findViewById<TextView>(R.id.past).text =
                "Past: ${list.count { it.dateTime?.toDate()?.after(now) != true }}";
                val groups = list.mapNotNull { it.dateTime?.toDate() }
                    .groupingBy { SimpleDateFormat("MMM", Locale.getDefault()).format(it) }
                    .eachCount(); findViewById<BarChart>(R.id.chart).apply {
                data = BarData(BarDataSet(groups.values.mapIndexed { i, v ->
                    BarEntry(
                        i.toFloat(),
                        v.toFloat()
                    )
                }, "Events per month")); invalidate()
            }
            }
        }
    }
}

class EventAdapter(private val click: (Event) -> Unit) : RecyclerView.Adapter<EventAdapter.VH>() {
    private var items = listOf<Event>();
    fun submit(v: List<Event>, query: String = "") {
        items = v.filter {
            query.isBlank() || it.title.contains(query, true) || it.description.contains(
                query,
                true
            ) || it.location.contains(query, true)
        }; notifyDataSetChanged()
    };
    override fun getItemCount() = items.size;
    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_event, p, false));

    override fun onBindViewHolder(h: VH, i: Int) = h.bind(items[i]);
    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(e: Event) {
            itemView.findViewById<TextView>(R.id.itemTitle).text =
                e.title; itemView.findViewById<TextView>(R.id.itemInfo).text =
                "${e.location} • ${e.dateTime?.toDate()}"; itemView.setOnClickListener { click(e) }
        }
    }
}
