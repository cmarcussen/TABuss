package test.BusTUC.Speech;

import java.io.File;
import java.util.ArrayList;

import test.BusTUC.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SpeechAnswer extends ListActivity
{
	private Context context;
	private ArrayList<String> answer;
	CBRAnswer current;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		context = this;
		// Get the extras from the Homescreen activity
		final Bundle extras = getIntent().getExtras();
		final CBRAnswer cbrAnsw = extras.getParcelable("cbr");
		final String speechAnsw = extras.getString("speech");
		current = new CBRAnswer();
		answer = new ArrayList <String>();
		// Set up list adapter
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
				context, R.layout.list_item, answer);
		setupList(answer, speechAnsw, cbrAnsw, listAdapter);
		final HTTP http = new HTTP();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// Get the user's coordinates from the Homescreen activity
				final double[] coords = extras.getDoubleArray("coords");
				if (position == 1 && cbrAnsw != null && coords != null)
				{
					AlertDialog.Builder alert2 = new AlertDialog.Builder(
							context);
					alert2.setMessage("U cool?");

					alert2.setPositiveButton("Yeah boy!",
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton)
								{
								}
							});
					alert2.setNegativeButton("Hell no!",
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int whichButton)
								{
									// Black list unwanted suggestion
									CBRAnswer newAnswer = blackList(coords[0],
											coords[1], context, http);
									if (newAnswer != null)
									{
										// Update list
										listAdapter.clear();
										setupList(answer, speechAnsw,
												newAnswer, listAdapter);
										listAdapter.notifyDataSetChanged();
									}
								}
							});

					alert2.show();
				}

			}
		});

	}

	public void setupList(ArrayList<String> answer, String speechAnsw,
			CBRAnswer cbrAnsw, ArrayAdapter<String> listAdapter)
	{
		if (speechAnsw != null && cbrAnsw != null && answer != null)
		{
			answer.clear();
			answer.add("ASR: " + speechAnsw);
			answer.add("CBR: " + cbrAnsw.getAnswer() + " Score: "
					+ cbrAnsw.getScore());
			setListAdapter(listAdapter);
			current = cbrAnsw;
		}
	}

	public CBRAnswer blackList(double lat, double lon, Context context,
			HTTP http)
	{
		return http.blackList(lat, lon, current.getAnswer(), context);
	}
}
