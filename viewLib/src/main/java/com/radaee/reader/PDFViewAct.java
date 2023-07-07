package com.radaee.reader;

import java.util.Locale;

import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.pdf.Page.Annotation;
import com.radaee.reader.Mp3PlayerActivity;
import com.radaee.reader.PDFLayoutView;
import com.radaee.reader.PDFLayoutView.PDFLayoutListener;
import com.radaee.reader.PDFViewController;
import com.radaee.reader.ShowWebView;
import com.radaee.reader.VideoPlayer;
import com.radaee.util.PDFAssetStream;
import com.radaee.util.PDFHttpStream;
import com.radaee.view.VPage;
import com.radaee.viewlib.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PDFViewAct extends Activity implements PDFLayoutListener, OnInitListener {
    Boolean Ttshide;
    String txtText,file_activities;
    private TextToSpeech tts;
    public static Document ms_tran_doc;
    static private int m_tmp_index = 0;
    private PDFAssetStream m_asset_stream = null;
    private PDFHttpStream m_http_stream = null;
    private Document m_doc = null;
    private RelativeLayout m_layout = null;
    private PDFLayoutView m_view = null;
    private PDFViewController m_controller = null;
    private boolean m_modified = false;
    private boolean need_save_doc = false;
    private void onFail(String msg)//treat open failed.
    {
        m_doc.Close();
        m_doc = null;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }
    private final void ProcessOpenResult(int ret) {
        switch (ret) {

            case -1://need input password
                onFail("Open Failed: Invalid Password");
                break;
            case -2://unknown encryption
                onFail("Open Failed: Unknown Encryption");
                break;
            case -3://damaged or invalid format
                onFail("Open Failed: Damaged or Invalid PDF file");
                break;
            case -10://access denied or invalid file path
                onFail("Open Failed: Access denied or Invalid path");
                break;
            case 0://succeeded, and continue
                m_view.PDFOpen(m_doc, this);
                m_controller = new PDFViewController(m_layout, m_view);
                break;
            default://unknown error
                onFail("Open Failed: Unknown Error");
                break;
        }
    }
    class MyPDFFontDel implements Document.PDFFontDelegate {
        @Override
        public String GetExtFont(String collection, String fname, int flag, int[] ret_flags) {
            Log.i("ExtFont", fname);
            return null;
        }
    }
    private MyPDFFontDel m_font_del = new MyPDFFontDel();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Global.Init(this);
        m_layout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.pdf_layout, null);
        m_view = (PDFLayoutView) m_layout.findViewById(R.id.pdf_view);
        Intent intent = getIntent();
        String bmp_format = intent.getStringExtra("BMPFormat");
        tts = new TextToSpeech(this, this);

        Ttshide = getIntent().getExtras().getBoolean("Ttshide");
        file_activities = getIntent().getStringExtra("file_activities");

        if (bmp_format != null) {
            if (bmp_format.compareTo("RGB_565") == 0)
                m_view.PDFSetBmpFormat(Bitmap.Config.RGB_565);
            else if (bmp_format.compareTo("ARGB_4444") == 0)
                m_view.PDFSetBmpFormat(Bitmap.Config.ARGB_4444);
        }
        if (ms_tran_doc != null) {
            m_doc = ms_tran_doc;
            ms_tran_doc = null;
            m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));//set temporary cache for editing.
            m_tmp_index++;
            m_view.PDFOpen(m_doc, this);
            m_controller = new PDFViewController(m_layout, m_view);
            need_save_doc = true;
        }
        else {
            String pdf_asset = intent.getStringExtra("PDFAsset");
            String pdf_path = intent.getStringExtra("PDFPath");
            String pdf_pswd = intent.getStringExtra("PDFPswd");
            String pdf_http = intent.getStringExtra("PDFHttp");
            if (pdf_http != null && pdf_http != "") {
                m_http_stream = new PDFHttpStream();
                m_http_stream.open(pdf_http);
                m_doc = new Document();
                int ret = m_doc.OpenStream(m_http_stream, pdf_pswd);
                ProcessOpenResult(ret);
            }
            else if (pdf_asset != null && pdf_asset != "") {
                m_asset_stream = new PDFAssetStream();
                m_asset_stream.open(getAssets(), pdf_asset);
                m_doc = new Document();
                int ret = m_doc.OpenStream(m_asset_stream, pdf_pswd);
                ProcessOpenResult(ret);
            }
            else if (pdf_path != null && pdf_path != "") {
                m_doc = new Document();
                int ret = m_doc.Open(pdf_path, pdf_pswd);
                m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));//set temporary cache for editing.
                m_tmp_index++;
                m_doc.SetFontDel(m_font_del);
                ProcessOpenResult(ret);
            }
        }
        setContentView(m_layout);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (m_doc == null)
            m_doc = m_view.PDFGetDoc();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        m_view.BundleSavePos(savedInstanceState);
        if (need_save_doc && m_doc != null) {
            Document.BundleSave(savedInstanceState, m_doc);//save Document object
            m_doc = null;
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (m_doc == null) {
            m_doc = Document.BundleRestore(savedInstanceState);//restore Document object
            m_view.PDFOpen(m_doc, this);
            m_controller = new PDFViewController(m_layout, m_view);
            need_save_doc = true;
        }
        m_view.BundleRestorePos(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        if (m_controller == null || m_controller.OnBackPressed()) {
            if (m_modified) {
                TextView txtView = new TextView(this);
                txtView.setText("Document modified\r\nDo you want save it?");
                new AlertDialog.Builder(this).setTitle("Exiting").setView(
                        txtView).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_doc.Save();
                        PDFViewAct.super.onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PDFViewAct.super.onBackPressed();
                    }
                }).show();
            } else super.onBackPressed();
        }
    }
    @SuppressLint("InlinedApi")
    @Override
    protected void onDestroy()
    {
        if (m_doc != null) {
            m_view.PDFClose();
            m_doc.Close();
            m_doc = null;
        }
        if (m_asset_stream != null) {
            m_asset_stream.close();
            m_asset_stream = null;
        }
        if (m_http_stream != null) {
            m_http_stream.close();
            m_http_stream = null;
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void OnPDFPageModified(int pageno) {

        m_modified = true;
    }
    @Override
    public void OnPDFPageChanged(int pageno) {
        if (m_controller != null)
            m_controller.OnPageChanged(pageno);
    }
    @Override
    public void OnPDFAnnotTapped(VPage vpage, Annotation annot) {
        if (annot != null && (annot.GetType() == 19 || annot.GetType() == 26 || annot.GetType() == 2 || annot.GetType() == 20)) //movie
        {
            m_view.PDFPerformAnnot();
        } else {
            if (m_controller != null)
                m_controller.OnAnnotTapped(annot);
        }
    }
    @Override
    public void OnPDFBlankTapped() {
        if (m_controller != null)
            m_controller.OnBlankTapped();
    }
    @Override
    public void OnPDFSelectEnd(String text) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dlg_text, null);
        final RadioGroup rad_group = (RadioGroup) layout.findViewById(R.id.rad_group);
        final String sel_text = text;
        RadioButton myRadioButton = (RadioButton) layout.findViewById(R.id.rad_Text_to_Speech);
        if (Ttshide == true) {
            myRadioButton.setVisibility(View.GONE);
        } else {
            myRadioButton.setVisibility(View.VISIBLE);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(DialogInterface dialog, int which) {
                if (rad_group.getCheckedRadioButtonId() == R.id.rad_copy) {
                    //	Toast.makeText(PDFViewAct.this, "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();

                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("RACHNA SAGAR", sel_text);
                    clipboard.setPrimaryClip(clip);
                }
                else if (m_doc.CanSave()) {
                    boolean ret = true;
                    if (rad_group.getCheckedRadioButtonId() == R.id.rad_copy) {
                        //    Toast.makeText(PDFViewAct.this, "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("RACHNA SAGAR", sel_text);
                        clipboard.setPrimaryClip(clip);
                    }
                    else if (rad_group.getCheckedRadioButtonId() == R.id.rad_highlight)
                        ret = m_view.PDFSetSelMarkup(0);
                    else if (rad_group.getCheckedRadioButtonId() == R.id.rad_underline)
                        ret = m_view.PDFSetSelMarkup(1);
                    else if (rad_group.getCheckedRadioButtonId() == R.id.rad_strikeout)
                        ret = m_view.PDFSetSelMarkup(2);
                    else if (rad_group.getCheckedRadioButtonId() == R.id.rad_squiggly)
                        ret = m_view.PDFSetSelMarkup(4);
                    else if (rad_group.getCheckedRadioButtonId() == R.id.rad_Text_to_Speech) {
                        ret = true;
                        txtText = sel_text;
                        speakOut();
                    }
                    if (!ret)
                        Toast.makeText(PDFViewAct.this, "add annotation failed!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PDFViewAct.this, "can't write or encrypted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (m_controller != null)
                    m_controller.OnSelectEnd();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle("Process selected text");
        builder.setCancelable(false);
        builder.setView(layout);
        AlertDialog dlg = builder.create();
        dlg.show();
    }
    @Override
    public void OnPDFOpenURI(String uri) {

        try {
	       /* Intent intent = new Intent();
	        intent.setAction("android.intent.action.VIEW");
	        Uri content_url = Uri.parse(uri);
	        intent.setData(content_url);
	        startActivity(intent);*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String uri1 = uri.substring(27);
                System.out.println("golaaa" + "    " + uri1);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(this, ShowWebView.class);
                intent.putExtra("Linkpass", uri1);
                Log.d("url34", "" + uri1);
                startActivity(intent);
            } else {
                String uri2 = uri.substring(27);
                System.out.println("golaaa" + "    " + uri2);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(this, ShowWebView.class);
                intent.putExtra("Linkpass", uri2);
                startActivity(intent);
                Log.d("url3456", "" + uri2);
            }
        } catch (Exception e) {
            //Toast.makeText(PDFViewAct.this, "todo: open url:" + uri, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void OnPDFOpenJS(String js) {
        //Toast.makeText(PDFViewAct.this, "todo: execute java script", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("SdCardPath")
    @Override
    public void OnPDFOpenMovie(String path) {
        //Toast.makeText(PDFViewAct.this, "todo: play movie", Toast.LENGTH_SHORT).show();
        Log.d("videoplayes19", "videoplay");
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("VIDEOPATH", path);
        startActivity(intent);
        Log.d("videoplayes20", "" + path);
    }
    @Override
    public void OnPDFOpenSound(int[] paras, String path) {
        //Toast.makeText(PDFViewAct.this, "todo: play sound", Toast.LENGTH_SHORT).show();
		/*
		VolumeKeyReceiver keyReceiver = new VolumeKeyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
		registerReceiver(keyReceiver, intentFilter);*/

        Intent intent = new Intent(this, Mp3PlayerActivity.class);
        intent.putExtra("AUDIOPATH", path);
        startActivity(intent);
		   /*MediaPlayer mPlayer = new MediaPlayer();
			Uri myUri = Uri.parse(path);
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mPlayer.setDataSource(getApplicationContext(), myUri);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mPlayer.start();*/
    }
    @Override
    public void OnPDFOpenAttachment(String path) {
        //Toast.makeText(PDFViewAct.this, "todo: treat attachment", Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(this, ShowWebView.class);
        startActivity(intent1);
    }
    @Override
    public void OnPDFOpen3D(String path) {
        //Toast.makeText(PDFViewAct.this, "todo: play 3D module", Toast.LENGTH_SHORT).show();
    }
    @TargetApi(Build.VERSION_CODES.DONUT)
    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);
            tts.setSpeechRate(0.80f);
            //tts.set
            //int result = tts.setLanguage(new Locale("hi-IN"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }
    @SuppressLint("NewApi")
    private void speakOut() {

        String text = txtText;
        try {
            if (text.length() == 0) {
                tts.speak("You haven't typed text", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        } catch (Exception e) {
        }
    }
}
