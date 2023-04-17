package com.radaee.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.radaee.viewlib.R;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
//import android.telephony.TelephonyManager;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

/**
 * class for Global setting.
 * 
 * @author Radaee
 * @version 1.1
 */
public class Global
{
	private static File sdDir;

	public static void Init(Activity act)
    {
        //second para is license type
        //            0 means standard license.
        //            1 means professional license
        //            2 means premium license
        //3rd para is company name string(not package name)
        //4th para is mail
        //5th para is key string
        //the package name got by native C/C++ library, not by pass parameter.
        Init( act, 2, "Rachna Sagar Pvt Ltd", "info@rachnasagar.in",
                "2LGLTQ-NLIM9C-2LE5AX-24YW5O-YONVOU-GZIIEO" );
       // 2LGLTQ-NLIM9C-2LE5AX-24YW5O-YONVOU-GZIIEO
//    	Init( act, 2, "rspl", "abc@gmail.com",
//                "AIFPFW-2S2JCQ-ZCYYNP-H70J2H-DU21RQ-LL3HVA" );
    }/*QZRORVH4CV2LDMHBK1MT9SR8*/
	/**
	 * get version string from library.
	 * @return version string, like: "201401"
	 */
	private static native String getVersion();
	private static native void setCMapsPath(String cmaps, String umaps);
	private static native boolean setCMYKICCPath(String path);
	private static native void fontfileListStart();
	private static native void fontfileListAdd(String font_file);
	private static native void fontfileListEnd();
	private static native void loadStdFont( int index, String path );

	/**
	 * map a face name to another name.<br/>
	 * invoke after fontfileListEnd and before setDefaultFont.
	 * 
	 * @param map_name
	 *            mapping name
	 * @param name
	 *            name in face-list, developer may list all face names by
	 *            getFaceCount and getFaceName
	 * @return false if name is not in face-list, or map_name is empty.
	 */
	private static native boolean fontfileMapping(String map_name, String name);

	private static native boolean setDefaultFont(String collection,
			String fontname, boolean fixed);

	private static native boolean setAnnotFont(String fontname);

	private static native int getFaceCount();

	private static native String getFaceName(int index);

	/**
	 * active license for premium version.<br/>
	 * this is full version for all features.
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activePremium(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for professional version.<br/>
	 * this is for annotation editing version but no form features.
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activeProfessional(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for standard version.<br/>
	 * this can't save and edit and no reflow function.
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activeStandard(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for premium version.<br/>
	 * this is full version for all features.<br/>
	 * the license for this method is binding to version string, see Global.getVersion();
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activePremiumForVer(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for professional version.<br/>
	 * this is for annotation editing version but no form features.<br/>
	 * the license for this method is binding to version string, see Global.getVersion();
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activeProfessionalForVer(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for standard version.<br/>
	 * this can't save and edit and no reflow function.<br/>
	 * the license for this method is binding to version string, see Global.getVersion();
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            address, example "radaee_com@yahoo.cn"
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activeStandardForVer(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * active license for time limit. features same as professional version, but
	 * actived only in date range from dt1 to dt2.
	 * 
	 * @param context
	 *            Context object
	 * @param company
	 *            company name, exapmle "radaee"
	 * @param mail
	 *            mail address, example "radaee_com@yahoo.cn"
	 * @param dt1
	 *            start date example "2012-12-31", must formated "yyyy-mm-dd" 10
	 *            length.
	 * @param dt2
	 *            end date example "2012-12-31", must formated "yyyy-mm-dd" 10
	 *            length.
	 * @param serial
	 *            serial number you got or buy.
	 * @return true or false
	 */
	private static native boolean activeTime(ContextWrapper context, String company,
			String mail, String dt1, String dt2, String serial);
	private static native boolean activeStandardTitanium(ContextWrapper context,
			String company, String mail, String serial);
	private static native boolean activeProfessionalTitanium(ContextWrapper context,
			String company, String mail, String serial);
	private static native boolean activePremiumTitanium(ContextWrapper context,
			String company, String mail, String serial);

	/**
	 * hide all annotations when render pages?
	 * 
	 * @param hide
	 *            true to hide, false to show.
	 */
	private static native void hideAnnots(boolean hide);

	private static native void drawScroll(Bitmap bmp, long dib1, long dib2, int x, int y, int style);
	/**
	 * not used for developer
	 */
	public static void DrawScroll(Bitmap bmp, DIB dib1, DIB dib2, int x, int y, int style)
	{
		drawScroll(bmp, dib1.hand, dib2.hand, x, y, style);
	}

	private static native void toDIBPoint(long matrix, float[] ppoint,
			float[] dpoint);

	private static native void toPDFPoint(long matrix, float[] dpoint,
			float[] ppoint);

	private static native void toDIBRect(long matrix, float[] prect,
			float[] drect);

	private static native void toPDFRect(long matrix, float[] drect,
			float[] prect);

	/**
	 * set annotation transparency color.<br/>
	 * default value: 0x200040FF
	 * 
	 * @param color
	 *            formated as 0xAARRGGBB
	 */
	private static native void setAnnotTransparency(int color);

	/**
	 * color for ink annotation
	 */
	public static int inkColor = 0x80404040;
	/**
	 * width for ink lines.
	 */
	public static float inkWidth = 4;
	/**
	 * color for rect annotation.
	 */
	public static int rectColor = 0x80C00000;
	/**
	 * selection color.
	 */
	public static int selColor = 0x400000C0;// selection color
	/**
	 * is text selection start from right to left in one line?
	 */
	public static boolean selRTOL = false;
	/**
	 * max zoom level; valid values: [2, 5]
	 */
	public static float zoomLevel = 3;
	public static float zoomStep = 1;
	/**
	 * fling distance: 0.5-2
	 */
	public static float fling_dis = 1.0f;// 0.5-2
	/**
	 * fling speed: 0.1-0.4
	 */
	public static float fling_speed = 0.2f;// 0.1 - 0.4
	/**
	 * default view:<br/>
	 * 0:vertical<br/>
	 * 2:scroll<br/>
	 * 3:single<br/>
	 * 4:SingleEx<br/>
	 * 5:ReFlow<br/>
	 * 6:2 page in landscape
	 */
	public static int def_view = 0;
	/**
	 * render mode: 0:draft 1:normal 2:best
	 */
	public static int render_mode = 2;
	/**
	 * render as dark mode?
	 */
	public static boolean dark_mode = false;

	/**
	 * temp path, able after Init() invoked
	 */
	public static String tmp_path = null;
	public static boolean need_time_span = true;

	static private void load_file(Resources res, int res_id, File save_file)
	{
		if( !save_file.exists() )
		{
			try
	    	{
		        int read;
				byte buf[] = new byte[4096];
				InputStream src = res.openRawResource(res_id );
    			FileOutputStream dst = new FileOutputStream( save_file );
   				while( (read = src.read( buf )) > 0 )
   					dst.write( buf, 0, read );
   				dst.close();
   				src.close();
   				dst = null;
   				src = null;
	    	}
			catch(Exception e)
			{
			}
		}
	}
	static private void load_std_font(Resources res, int res_id, int index, File dst)
	{
		load_file(res, res_id, dst);
		loadStdFont( index, dst.getPath() );
	}
    static private void load_truetype_font(Resources res, int res_id, File dst)
    {
        load_file(res, res_id, dst);
        fontfileListAdd( dst.getPath() );
    }
	static private boolean load_cmyk_icc(Resources res, int res_id, File dst)
	{
		load_file(res, res_id, dst);
		return setCMYKICCPath(dst.getPath());
	}
	static private void load_cmaps(Resources res, int res_cmap, File dst_cmap, int res_umap, File dst_umap)
	{
		load_file(res, res_cmap, dst_cmap);
		load_file(res, res_umap, dst_umap);
		setCMapsPath(dst_cmap.getPath(), dst_umap.getPath());
	}
	static private boolean ms_init = false;
	/**
	 * global initialize function. it load JNI library and write some data to memory.
	 * @param act Activity need input, native get package name from this Activity, and then check package name.
	 * @param license_type 1: standard license, 2: professional license, 3: premium license.
	 * @param company_name
	 * @param mail
	 * @param serial
	 * @return
	 */

	public static boolean Init(Activity act, int license_type, String company_name, String mail, String serial)
	{
		if(ms_init) return true;
		if( act == null ) return false;
 		// load library
		System.loadLibrary("rdpdf");
		// save resource to sand-box for application.
		File files = new File(act.getFilesDir(), "rdres");
		if (!files.exists())// not exist? make it!
			files.mkdir();
		Resources res = act.getResources();
        load_std_font( res, R.raw.rdf008, 8, new File(files, "rdf008") );
        load_std_font( res, R.raw.rdf013, 13, new File(files, "rdf013") );
		load_cmyk_icc( res, R.raw.cmyk_rgb, new File(files, "cmyk_rgb") );
		load_cmaps( res, R.raw.cmaps, new File(files, "cmaps"), R.raw.umaps, new File(files, "umaps") );

		// create temporary dictionary, to save media or attachment data.

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			sdDir = new File("/storage/emulated/0/Android/data/com.rachnasagar/files/Documents/");
			Log.d("fett1",""+sdDir);

		}else{
			  sdDir = Environment.getExternalStorageDirectory();
		}

		if (sdDir != null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			files = new File(sdDir, "rdtmp");
		else
			files = new File(act.getFilesDir(), "rdtmp");
		if (!files.exists())// not exist? make it!
			files.mkdir();
		tmp_path = files.getPath();

		switch(license_type)
		{
		case 1:
			ms_init = activeProfessional(act, company_name, mail, serial);
			break;
		case 2:
			ms_init = activePremium(act, company_name, mail, serial);
			break;
		default:
			ms_init = activeStandard(act, company_name, mail, serial);
			break;
		}
		// active library, or WaterMark will displayed on each page.
		// boolean succeeded = activeStandard(act, "radaee",
		// "radaee_com@yahoo.cn", "HV8A19-WOT9YC-9ZOU9E-OQ31K2-FADG6Z-XEBCAO");
		// boolean succeeded = activeProfessional( act, "radaee",
		// "radaee_com@yahoo.cn", "Z5A7JV-5WQAJY-9ZOU9E-OQ31K2-FADG6Z-XEBCAO" );
		//boolean succeeded = activePremium(act, "radaee", "radaee_com@yahoo.cn",
		//		"LNJFDN-C89QFX-9ZOU9E-OQ31K2-FADG6Z-XEBCAO");

		// active library, or WaterMark will displayed on each page.
		// these active function is binding to version string "201401".
		//String ver = getVersion();
		 boolean succeeded = activeStandardForVer(act, "Rachna Sagar Pvt Ltd",
		 "info@rachnasagar.in", "2LGLTQ-NLIM9C-2LE5AX-24YW5O-YONVOU-GZIIEO");
		// boolean succeeded = activeProfessionalForVer(act, "Radaee",
		// "radaeepdf@gmail.com", "6D7KV9-FYCVAE-H3CRUZ-WAJQ9H-5R5V9L-KM0Y1L" );
		// boolean succeeded = activePremiumForVer(act, "Radaee", "radaeepdf@gmail.com",
		//		"Q6EL00-BTB1EG-H3CRUZ-WAJQ9H-5R5V9L-KM0Y1L");

		// add system external fonts.
		fontfileListStart();
		fontfileListAdd("/system/fonts/DroidSans.ttf");
		fontfileListAdd("/system/fonts/Roboto-Regular.ttf");
		fontfileListAdd("/system/fonts/DroidSansFallback.ttf");
		fontfileListAdd("/system/fonts/bitsumis.ttf");
		fontfileListAdd("/system/fonts/ald721b_3.ttf");
		fontfileListAdd("/system/fonts/ald721bi_1.TTF");
		fontfileListAdd("/system/fonts/ald721i_2.TTF");
		fontfileListAdd("/system/fonts/ald721sn.ttf");
		fontfileListAdd("/system/fonts/anyquab_9.TTF");
		fontfileListAdd("/system/fonts/antquabi.TTF");
		fontfileListAdd("/system/fonts/antquai_5.TTF");
		fontfileListAdd("/system/fonts/arial_14.ttf");
		fontfileListAdd("/system/fonts/arialbd_15.ttf");
		fontfileListAdd("/system/fonts/arialbi_5.ttf");
		fontfileListAdd("/system/fonts/ariali_4.TTF");
		fontfileListAdd("/system/fonts/banff_normal.ttf");
		fontfileListAdd("/system/fonts/bkant_9.TTF");
		fontfileListAdd("/system/fonts/bookos.TTF");
		fontfileListAdd("/system/fonts/bookosb.TTF");
		fontfileListAdd("/system/fonts/bookosbi.TTF");
		fontfileListAdd("/system/fonts/bookosi.TTF");
		fontfileListAdd("/system/fonts/calibri_c.ttf");
		fontfileListAdd("/system/fonts/calibrib_b.TTF");
		fontfileListAdd("/system/fonts/calibrii_5.ttf");
		fontfileListAdd("/system/fonts/calibriz_5.ttf");
		fontfileListAdd("/system/fonts/century.TTF");
		fontfileListAdd("/system/fonts/cgor45w_0.TTF");
		fontfileListAdd("/system/fonts/cgor46w_0.ttf");
		fontfileListAdd("/system/fonts/cgor65w_0.ttf");
		fontfileListAdd("/system/fonts/cgor66w_0.ttf");
		fontfileListAdd("/system/fonts/coopbl_0.TTF");
		fontfileListAdd("/system/fonts/dixiland_1.TTF");
		fontfileListAdd("/system/fonts/dut801b.TTF");
		fontfileListAdd("/system/fonts/dut801bi.TTF");
		fontfileListAdd("/system/fonts/dut801i.TTF");
		fontfileListAdd("/system/fonts/dut801n.TTF");
		fontfileListAdd("/system/fonts/ex0462.TTF");
		fontfileListAdd("/system/fonts/gara.TTF");
		fontfileListAdd("/system/fonts/garabd.TTF");
		fontfileListAdd("/system/fonts/garait.TTF");
		fontfileListAdd("/system/fonts/garr66w_0.ttf");
		fontfileListAdd("/system/fonts/gil.TTF");
		fontfileListAdd("/system/fonts/gilb.TTF");
		fontfileListAdd("/system/fonts/gilbi.TTF");
		fontfileListAdd("/system/fonts/gili.TTF");
		fontfileListAdd("/system/fonts/gothic.TTF");
		fontfileListAdd("/system/fonts/gothicb.TTF");
		fontfileListAdd("/system/fonts/gothicbi.TTF");
		fontfileListAdd("/system/fonts/gothici.TTF");
		fontfileListAdd("/system/fonts/impact_2.TTF");
		fontfileListAdd("/system/fonts/ncsr55w_2.TTF");
		fontfileListAdd("/system/fonts/ncsr56w_2.TTF");
		fontfileListAdd("/system/fonts/ncsr75w_3.TTF");
		fontfileListAdd("/system/fonts/ncsr76w_1.ttf");
		fontfileListAdd("/system/fonts/times.ttf");
		fontfileListAdd("/system/fonts/timesbd.ttf");
		fontfileListAdd("/system/fonts/timesbi_4.TTF");
		fontfileListAdd("/system/fonts/timesi_4.TTF");
		fontfileListAdd("/system/fonts/tt0032m0.TTF");
		fontfileListAdd("/system/fonts/tt0033m.ttf");
		fontfileListAdd("/system/fonts/tt0034m.ttf");
		fontfileListAdd("/system/fonts/tt0861m.ttf");
		fontfileListAdd("/system/fonts/ufonts_com_baskerville_bt_roman.ttf");
		fontfileListAdd("/system/fonts/webdings.ttf");
		fontfileListAdd("/system/fonts/acaslonpro_italic.otf");
		fontfileListAdd("/system/fonts/acaslonpro_semibold.otf");
		fontfileListAdd("/system/fonts/acaslonpro_semibolditalic.otf");
		fontfileListAdd("/system/fonts/adobegothicstd_bold.otf");
		fontfileListAdd("/system/fonts/agaramondpro_italic.otf");
		fontfileListAdd("/system/fonts/agaramondpro_regular.otf");
		fontfileListAdd("/system/fonts/bellgothicstd_bold.otf");
		fontfileListAdd("/system/fonts/banffn.PFB");
		fontfileListAdd("/system/fonts/censbkn_1.PFB");
		fontfileListAdd("/system/fonts/chanakya.PFB");
		fontfileListAdd("/system/fonts/chanakyb.PFB");
		fontfileListAdd("/system/fonts/futura4.PFB");
		fontfileListAdd("/system/fonts/gdi.PFB");
		fontfileListAdd("/system/fonts/gdrg.PFB");
		fontfileListAdd("/system/fonts/gdsb.PFB");
		fontfileListAdd("/system/fonts/hvb.PFB");
		fontfileListAdd("/system/fonts/tib.PFB");
		fontfileListAdd("/system/fonts/tir.PFB");
		fontfileListAdd("/system/fonts/wacb.PFB");
		fontfileListAdd("/system/fonts/wacbi.PFB");
		fontfileListAdd("/system/fonts/wacn.PFB");
		fontfileListAdd("/system/fonts/wacni.PFB");
		load_truetype_font( res, R.raw.wacni, new File(files, "wacni.PFB") );
		load_truetype_font( res, R.raw.wacn, new File(files, "wacn.PFB") );
		load_truetype_font( res, R.raw.wacbi, new File(files, "wacbi.PFB") );
		load_truetype_font( res, R.raw.wacb, new File(files, "wacb.PFB") );
		load_truetype_font( res, R.raw.tir, new File(files, "tir.PFB") );
		load_truetype_font( res, R.raw.tib, new File(files, "tib.PFB") );	
		load_truetype_font( res, R.raw.hvb, new File(files, "hvb.PFB") );	
		load_truetype_font( res, R.raw.gdsb, new File(files, "gdsb.PFB") );		
		load_truetype_font( res, R.raw.gdrg, new File(files, "gdrg.PFB") );
		load_truetype_font( res, R.raw.gdi, new File(files, "gdi.PFB") );
		load_truetype_font( res, R.raw.futura4, new File(files, "futura4.PFB") );
		load_truetype_font( res, R.raw.chanakyb, new File(files, "chanakyb.PFB") );
		load_truetype_font( res, R.raw.chanakya, new File(files, "chanakya.PFB") );
		load_truetype_font( res, R.raw.censbkn_1, new File(files, "censbkn_1.PFB") );
		load_truetype_font( res, R.raw.banffn, new File(files, "banffn.PFB") );
		load_truetype_font( res, R.raw.bellgothicstd_bold, new File(files, "bellgothicstd_bold.otf") );
		load_truetype_font( res, R.raw.agaramondpro_regular, new File(files, "agaramondpro_regular.otf") );
		load_truetype_font( res, R.raw.agaramondpro_italic, new File(files, "agaramondpro_italic.otf") );
		load_truetype_font( res, R.raw.adobegothicstd_bold, new File(files, "adobegothicstd_bold.otf") );
		load_truetype_font( res, R.raw.acaslonpro_semibolditalic, new File(files, "acaslonpro_semibolditalic.otf") );
		load_truetype_font( res, R.raw.acaslonpro_semibold, new File(files, "acaslonpro_semibold.otf") );
		load_truetype_font( res, R.raw.acaslonpro_italic, new File(files, "acaslonpro_italic.otf") );
		load_truetype_font( res, R.raw.webdings, new File(files, "webdings.ttf") );
		load_truetype_font( res, R.raw.ufonts_com_baskerville_bt_roman, new File(files, "ufonts_com_baskerville_bt_roman.ttf") );
		load_truetype_font( res, R.raw.tt0861m, new File(files, "tt0861m.ttf") );
		load_truetype_font( res, R.raw.tt0034m, new File(files, "tt0034m.ttf") );
		load_truetype_font( res, R.raw.tt0033m, new File(files, "tt0033m.ttf") );
		load_truetype_font( res, R.raw.tt0032m0, new File(files, "tt0032m0.TTF") );
		load_truetype_font( res, R.raw.timesi_4, new File(files, "timesi_4.TTF") );
		load_truetype_font( res, R.raw.timesbi_4, new File(files, "timesbi_4.TTF") );
		load_truetype_font( res, R.raw.timesbd, new File(files, "timesbd.ttf") );
		load_truetype_font( res, R.raw.times, new File(files, "times.ttf") );
		load_truetype_font( res, R.raw.ncsr76w_1, new File(files, "ncsr76w_1.ttf") );
		load_truetype_font( res, R.raw.ncsr75w_3, new File(files, "ncsr75w_3.TTF") );
		load_truetype_font( res, R.raw.ncsr56w_2, new File(files, "ncsr56w_2.TTF") );
		load_truetype_font( res, R.raw.ncsr55w_2, new File(files, "ncsr55w_2.TTF") );
		load_truetype_font( res, R.raw.impact_2, new File(files, "impact_2.TTF") );
		load_truetype_font( res, R.raw.gothici, new File(files, "gothici.TTF") );
		load_truetype_font( res, R.raw.gothicbi, new File(files, "gothicbi.TTF") );
		load_truetype_font( res, R.raw.gothicb, new File(files, "gothicb.TTF") );
		load_truetype_font( res, R.raw.gothic, new File(files, "gothic.TTF") );
		load_truetype_font( res, R.raw.gili, new File(files, "gili.TTF") );
		load_truetype_font( res, R.raw.gilbi, new File(files, "gilbi.TTF") );
		load_truetype_font( res, R.raw.gilb, new File(files, "gilb.TTF") );
		load_truetype_font( res, R.raw.gil, new File(files, "gil.TTF") );
		load_truetype_font( res, R.raw.garr66w_0, new File(files, "garr66w_0.ttf") );
		load_truetype_font( res, R.raw.garait, new File(files, "garait.TTF") );
		load_truetype_font( res, R.raw.garabd, new File(files, "garabd.TTF") );
		load_truetype_font( res, R.raw.gara, new File(files, "gara.TTF") );
		load_truetype_font( res, R.raw.ex0462, new File(files, "ex0462.TTF") );
		load_truetype_font( res, R.raw.dut801n, new File(files, "dut801n.TTF") );
		load_truetype_font( res, R.raw.dut801i, new File(files, "dut801i.TTF") );
		load_truetype_font( res, R.raw.dut801bi, new File(files, "dut801bi.TTF") );
		load_truetype_font( res, R.raw.dut801b, new File(files, "dut801b.TTF") );
		load_truetype_font( res, R.raw.dixiland_1, new File(files, "dixiland_1.TTF") );
		load_truetype_font( res, R.raw.coopbl_0, new File(files, "coopbl_0.TTF") );
		load_truetype_font( res, R.raw.cgor66w_0, new File(files, "cgor66w_0.ttf") );
		load_truetype_font( res, R.raw.cgor65w_0, new File(files, "cgor65w_0.ttf") );
		load_truetype_font( res, R.raw.cgor46w_0, new File(files, "cgor46w_0.ttf") );
		load_truetype_font( res, R.raw.cgor45w_0, new File(files, "cgor45w_0.TTF") );
		load_truetype_font( res, R.raw.century, new File(files, "century.TTF") );
		load_truetype_font( res, R.raw.calibriz_5, new File(files, "calibriz_5.ttf") );
		load_truetype_font( res, R.raw.calibrii_5, new File(files, "calibrii_5.ttf") );
		load_truetype_font( res, R.raw.calibrib_b, new File(files, "calibrib_b.TTF") );
		load_truetype_font( res, R.raw.calibri_c, new File(files, "calibri_c.ttf") );
		load_truetype_font( res, R.raw.bookosi, new File(files, "bookosi.TTF") );
		load_truetype_font( res, R.raw.bookosbi, new File(files, "bookosbi.TTF") );
		load_truetype_font( res, R.raw.bookosb, new File(files, "bookosb.TTF") );
		load_truetype_font( res, R.raw.bookos, new File(files, "bookos.TTF") );
		load_truetype_font( res, R.raw.bkant_9, new File(files, "bkant_9.TTF") );
		load_truetype_font( res, R.raw.banff_normal, new File(files, "banff_normal.ttf") );
		load_truetype_font( res, R.raw.ariali_4, new File(files, "ariali_4.TTF") );
		load_truetype_font( res, R.raw.arialbi_5, new File(files, "arialbi_5.ttf") );
		load_truetype_font( res, R.raw.arialbd_15, new File(files, "arialbd_15.ttf") );
		load_truetype_font( res, R.raw.arial_14, new File(files, "arial_14.ttf") );
		load_truetype_font( res, R.raw.antquai_5, new File(files, "antquai_5.TTF") );
		load_truetype_font( res, R.raw.antquabi, new File(files, "antquabi.TTF") );
		load_truetype_font( res, R.raw.anyquab_9, new File(files, "anyquab_9.TTF") );
		load_truetype_font( res, R.raw.ald721sn, new File(files, "ald721sn.ttf") );
		load_truetype_font( res, R.raw.ald721i_2, new File(files, "ald721i_2.TTF") );
		load_truetype_font( res, R.raw.ald721bi_1, new File(files, "ald721bi_1.TTF") );
		load_truetype_font( res, R.raw.ald721b_3, new File(files, "ald721b_3.ttf") );
		load_truetype_font( res, R.raw.bitsumis, new File(files, "bitsumis.ttf") );
        load_truetype_font( res, R.raw.arimo, new File(files, "arimo.ttf") );
        load_truetype_font( res, R.raw.arimob, new File(files, "arimob.ttf") );
        load_truetype_font( res, R.raw.arimoi, new File(files, "arimoi.ttf") );
        load_truetype_font( res, R.raw.arimobi, new File(files, "arimobi.ttf") );
        load_truetype_font( res, R.raw.tinos, new File(files, "tinos.ttf") );
        load_truetype_font( res, R.raw.tinosb, new File(files, "tinosb.ttf") );
        load_truetype_font( res, R.raw.tinosi, new File(files, "tinosi.ttf") );
        load_truetype_font( res, R.raw.tinosbi, new File(files, "tinosbi.ttf") );
        load_truetype_font( res, R.raw.cousine, new File(files, "cousine.ttf") );
        load_truetype_font( res, R.raw.cousineb, new File(files, "cousineb.ttf") );
        load_truetype_font( res, R.raw.cousinei, new File(files, "cousinei.ttf") );
        load_truetype_font( res, R.raw.cousinebi, new File(files, "cousinebi.ttf") );
		fontfileListEnd();
        fontfileMapping("Arial",                    "Arimo");
        fontfileMapping("Arial Bold",              "Arimo Bold");
        fontfileMapping("Arial BoldItalic",       "Arimo Bold Italic");
        fontfileMapping("Arial Italic",            "Arimo Italic");
        fontfileMapping("Arial,Bold",              "Arimo Bold");
        fontfileMapping("Arial,BoldItalic",       "Arimo Bold Italic");
        fontfileMapping("Arial,Italic",            "Arimo Italic");
        fontfileMapping("Arial-Bold",              "Arimo Bold");
        fontfileMapping("Arial-BoldItalic",       "Arimo Bold Italic");
        fontfileMapping("Arial-Italic",            "Arimo Italic");
        fontfileMapping("ArialMT",                  "Arimo");
        fontfileMapping("Calibri",                  "Arimo");
        fontfileMapping("Calibri Bold",            "Arimo Bold");
        fontfileMapping("Calibri BoldItalic",      "Arimo Bold Italic");
        fontfileMapping("Calibri Italic",           "Arimo Italic");
        fontfileMapping("Calibri,Bold",             "Arimo Bold");
        fontfileMapping("Calibri,BoldItalic",      "Arimo Bold Italic");
        fontfileMapping("Calibri,Italic",           "Arimo Italic");
        fontfileMapping("Calibri-Bold",             "Arimo Bold");
        fontfileMapping("Calibri-BoldItalic",      "Arimo Bold Italic");
        fontfileMapping("Calibri-Italic",           "Arimo Italic");
        fontfileMapping("Helvetica",                "Arimo");
        fontfileMapping("Helvetica Bold",          "Arimo Bold");
        fontfileMapping("Helvetica BoldItalic",   "Arimo Bold Italic");
        fontfileMapping("Helvetica Italic",        "Arimo Italic");
        fontfileMapping("Helvetica,Bold",          "Arimo,Bold");
        fontfileMapping("Helvetica,BoldItalic",   "Arimo Bold Italic");
        fontfileMapping("Helvetica,Italic",        "Arimo Italic");
        fontfileMapping("Helvetica-Bold",          "Arimo Bold");
        fontfileMapping("Helvetica-BoldItalic",   "Arimo Bold Italic");
        fontfileMapping("Helvetica-Italic",        "Arimo Italic");
        fontfileMapping("Garamond",                    "Tinos");
        fontfileMapping("Garamond,Bold",              "Tinos Bold");
        fontfileMapping("Garamond,BoldItalic",       "Tinos Bold Italic");
        fontfileMapping("Garamond,Italic",            "Tinos Italic");
        fontfileMapping("Garamond-Bold",              "Tinos Bold");
        fontfileMapping("Garamond-BoldItalic",       "Tinos Bold Italic");
        fontfileMapping("Garamond-Italic",            "Tinos Italic");
        fontfileMapping("Times",                    "Tinos");
        fontfileMapping("Times,Bold",              "Tinos Bold");
        fontfileMapping("Times,BoldItalic",       "Tinos Bold Italic");
        fontfileMapping("Times,Italic",            "Tinos Italic");
        fontfileMapping("Times-Bold",              "Tinos Bold");
        fontfileMapping("Times-BoldItalic",       "Tinos Bold Italic");
        fontfileMapping("Times-Italic",            "Tinos Italic");
        fontfileMapping("Times-Roman",             "Tinos");
        fontfileMapping("Times New Roman",                "Tinos");
        fontfileMapping("Times New Roman,Bold",          "Tinos Bold");
        fontfileMapping("Times New Roman,BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("Times New Roman,Italic",        "Tinos Italic");
        fontfileMapping("Times New Roman-Bold",          "Tinos Bold");
        fontfileMapping("Times New Roman-BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("Times New Roman-Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRoman",                "Tinos");
        fontfileMapping("TimesNewRoman,Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRoman,BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRoman,Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRoman-Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRoman-BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRoman-Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRomanPS",                "Tinos");
        fontfileMapping("TimesNewRomanPS,Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRomanPS,BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRomanPS,Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRomanPS-Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRomanPS-BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRomanPS-Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRomanPSMT",                "Tinos");
        fontfileMapping("TimesNewRomanPSMT,Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRomanPSMT,BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRomanPSMT,Italic",        "Tinos Italic");
        fontfileMapping("TimesNewRomanPSMT-Bold",          "Tinos Bold");
        fontfileMapping("TimesNewRomanPSMT-BoldItalic",   "Tinos Bold Italic");
        fontfileMapping("TimesNewRomanPSMT-Italic",        "Tinos Italic");
        fontfileMapping("Courier",                    "Cousine");
        fontfileMapping("Courier Bold",              "Cousine Bold");
        fontfileMapping("Courier BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier Italic",            "Cousine Italic");
        fontfileMapping("Courier,Bold",              "Cousine Bold");
        fontfileMapping("Courier,BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier,Italic",            "Cousine Italic");
        fontfileMapping("Courier-Bold",              "Cousine Bold");
        fontfileMapping("Courier-BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier-Italic",            "Cousine Italic");
        fontfileMapping("Courier New",                    "Cousine");
        fontfileMapping("Courier New Bold",              "Cousine Bold");
        fontfileMapping("Courier New BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier New Italic",            "Cousine Italic");
        fontfileMapping("Courier New,Bold",              "Cousine Bold");
        fontfileMapping("Courier New,BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier New,Italic",            "Cousine Italic");
        fontfileMapping("Courier New-Bold",              "Cousine Bold");
        fontfileMapping("Courier New-BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("Courier New-Italic",            "Cousine Italic");
        fontfileMapping("CourierNew",                    "Cousine");
        fontfileMapping("CourierNew Bold",              "Cousine Bold");
        fontfileMapping("CourierNew BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("CourierNew Italic",            "Cousine Italic");
        fontfileMapping("CourierNew,Bold",              "Cousine Bold");
        fontfileMapping("CourierNew,BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("CourierNew,Italic",            "Cousine Italic");
        fontfileMapping("CourierNew-Bold",              "Cousine Bold");
        fontfileMapping("CourierNew-BoldItalic",       "Cousine Bold Italic");
        fontfileMapping("CourierNew-Italic",            "Cousine Italic");
        //fontfileMapping("TimesNewRoman", "Times New Roman");

		String face_name = null;
		int face_first = 0;
		int face_count = getFaceCount();
		while (face_first < face_count)
		{
			face_name = getFaceName(face_first);
			if (face_name != null) break;
			face_first++;
		}

		
		/*File file = new File("/system/fonts/bitsumis.ttf");
		if(file.exists())
		   fontfileListAdd("/system/fonts/bitsumis.ttf");
		else
		   load_truetype_font(res, R.raw.bitsumis, new File(files, "bitsumis.ttf")); */
		 
		// set default font for fixed width font.
		if (!setDefaultFont(null, "Roboto-Regular", true) && face_name != null)
		{
			if( !setDefaultFont(null, "DroidSans", true) )
				setDefaultFont(null, face_name, true);
		}
		// set default font for non-fixed width font.
		if (!setDefaultFont(null, "Roboto-Regular", false) && face_name != null)
		{
			if( !setDefaultFont(null, "DroidSans", false) )
				setDefaultFont(null, face_name, false);
		}

		// set default font for Chinese simplified language.
		if (!setDefaultFont("GB1", "DroidSansFallback", true)
				&& face_name != null)
			setDefaultFont(null, face_name, true);
		if (!setDefaultFont("GB1", "DroidSansFallback", false)
				&& face_name != null)
			setDefaultFont(null, face_name, false);

		// set default font for Chinese traditional language.
		if (!setDefaultFont("CNS1", "DroidSansFallback", true)
				&& face_name != null)
			setDefaultFont(null, face_name, true);
		if (!setDefaultFont("CNS1", "DroidSansFallback", false)
				&& face_name != null)
			setDefaultFont(null, face_name, false);

		// set default font for Japanese.
		if (!setDefaultFont("Japan1", "DroidSansFallback", true)
				&& face_name != null)
			setDefaultFont(null, face_name, true);
		if (!setDefaultFont("Japan1", "DroidSansFallback", false)
				&& face_name != null)
			setDefaultFont(null, face_name, false);

		// set default font for Korean.
		if (!setDefaultFont("Korea1", "DroidSansFallback", true)
				&& face_name != null)
			setDefaultFont(null, face_name, true);
		if (!setDefaultFont("Korea1", "DroidSansFallback", false)
				&& face_name != null)
			setDefaultFont(null, face_name, false);

		if (!setAnnotFont("DroidSansFallback") && face_name != null) {
			setAnnotFont(face_name);
		}

		// set text font for edit-box and combo-box editing.
		if (!setAnnotFont("DroidSansFallback") && face_name != null) {
			setAnnotFont(face_name);
		}

		// set configure to default value
		default_config();
		
		
		System.out.println("sdsdddd"+"     "+ms_init);
		return ms_init;
	}

	/**
	 * reset to default configure.
	 */
	public static void default_config()
	{
		selColor = 0x400000C0;// selection color
		fling_dis = 1.0f;// 0.5-2
		fling_speed = 0.1f;// 0.05 - 0.2
		def_view = 0;// 0,1,2,3,4,5,6 0:vertical 1:horizon 2:curl effect 3:single
						// 4:SingleEx 5:Reflow, 6:show 2 page as 1 page in land
						// scape mode
		render_mode = 1;// 0,1,2 0:draft 1:normal 2:best
		dark_mode = false;// dark mode
		zoomLevel = 3;
		need_time_span = true;
		//setAnnotTransparency(0x200040FF);
		setAnnotTransparency(0x00000000);
	}

	/**
	 * map PDF point to DIB point.
	 * 
	 * @param mat
	 *            Matrix object defined scale, rotate, tranlate operations.
	 * @param ppoint
	 *            input point in PDF coordinate system. [x, y]
	 * @param dpoint
	 *            output point in DIB coordinate system. [x, y]
	 */
	public static void ToDIBPoint(Matrix mat, float[] ppoint, float[] dpoint) {
		toDIBPoint(mat.hand, ppoint, dpoint);
	}

	/**
	 * map DIB point to PDF point.
	 * 
	 * @param mat
	 *            Matrix object defined scale, rotate, tranlate operations.
	 * @param dpoint
	 *            input point in DIB coordinate system. [x, y]
	 * @param ppoint
	 *            output point in PDF coordinate system. [x, y]
	 */
	public static void ToPDFPoint(Matrix mat, float[] dpoint, float[] ppoint) {
		toPDFPoint(mat.hand, dpoint, ppoint);
	}

	/**
	 * map PDF rectangle to DIB rectangle.
	 * 
	 * @param mat
	 *            Matrix object defined scale, rotate, tranlate operations.
	 * @param prect
	 *            input rect in PDF coordinate system. [left, top, right,
	 *            bottom]
	 * @param drect
	 *            output rect in DIB coordinate system. [left, top, right,
	 *            bottom]
	 */
	public static void ToDIBRect(Matrix mat, float[] prect, float[] drect) {
		toDIBRect(mat.hand, prect, drect);
	}

	/**
	 * map DIB rectangle to PDF rectangle.
	 * 
	 * @param mat
	 *            Matrix object defined scale, rotate, tranlate operations.
	 * @param drect
	 *            input rect in DIB coordinate system. [left, top, right,
	 *            bottom]
	 * @param prect
	 *            output rect in PDF coordinate system. [left, top, right,
	 *            bottom]
	 */
	public static void ToPDFRect(Matrix mat, float[] drect, float[] prect) {
		toPDFRect(mat.hand, drect, prect);
	}

	/**
	 * map PDF point to DIB point.
	 * 
	 * @param ratio
	 *            scale value apply to page rendering.
	 * @param dib_h
	 *            height of render bitmap.
	 * @param ppoint
	 *            input point in PDF coordinate system. [x, y]
	 * @param dpoint
	 *            output point in DIB coordinate system. [x, y]
	 */
	public static void ToDIBPoint(float ratio, int dib_h, float[] ppoint,
			float[] dpoint) {
		dpoint[0] = ppoint[0] * ratio;
		dpoint[1] = dib_h - ppoint[1] * ratio;
	}

	/**
	 * map DIB point to PDF point.
	 * 
	 * @param ratio
	 *            scale value apply to page rendering.
	 * @param dib_h
	 *            height of render bitmap.
	 * @param dpoint
	 *            input point in DIB coordinate system. [x, y]
	 * @param ppoint
	 *            output point in PDF coordinate system. [x, y]
	 */
	public static void ToPDFPoint(float ratio, int dib_h, float[] dpoint,
			float[] ppoint) {
		ppoint[0] = dpoint[0] / ratio;
		ppoint[1] = (dib_h - dpoint[1]) / ratio;
	}

	/**
	 * map PDF rectangle to DIB rectangle.
	 * 
	 * @param ratio
	 *            scale value apply to page rendering.
	 * @param dib_h
	 *            height of render bitmap.
	 * @param prect
	 *            input rect in PDF coordinate system. [left, top, right,
	 *            bottom]
	 * @param drect
	 *            output rect in DIB coordinate system. [left, top, right,
	 *            bottom]
	 */
	public static void ToDIBRect(float ratio, int dib_h, float[] prect,
			float[] drect) {
		drect[0] = prect[0] * ratio;
		drect[1] = dib_h - prect[3] * ratio;
		drect[2] = prect[2] * ratio;
		drect[3] = dib_h - prect[1] * ratio;
	}

	/**
	 * map DIB rectangle to PDF rectangle.
	 * 
	 * @param ratio
	 *            scale value apply to page rendering.
	 * @param dib_h
	 *            height of render bitmap.
	 * @param drect
	 *            input rect in DIB coordinate system. [left, top, right,
	 *            bottom]
	 * @param prect
	 *            output rect in PDF coordinate system. [left, top, right,
	 *            bottom]
	 */
	public static void ToPDFRect(float ratio, int dib_h, float[] drect,
			float[] prect) {
		prect[0] = drect[0] / ratio;
		prect[1] = (dib_h - drect[3]) / ratio;
		prect[2] = drect[2] / ratio;
		prect[3] = (dib_h - drect[1]) / ratio;
	}

	/**
	 * remove all tmp files that "pdfex" library generated.
	 */
	public static void RemoveTmp()
	{
		try
		{
			File tmp = new File(tmp_path);
			File files[] = tmp.listFiles();
			if (files != null)
			{
				for (int index = 0; index < files.length; index++)
					files[index].delete();
				tmp.delete();
			}
		}
		catch(Exception e)
		{
		}
	}
}
