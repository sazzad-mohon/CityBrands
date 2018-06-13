/**
 * 
 */
package com.shehala.citybrands.util;

import java.lang.reflect.InvocationTargetException;

import android.webkit.WebView;

/**
 * @author Raj
 *
 */
public class PauseVideoTimer {

	public static void pauseVideo(WebView v) {
		try {
			Class.forName("android.webkit.WebView")
					.getMethod("onPause", (Class[]) null)
					.invoke(v, (Object[]) null);
			v.pauseTimers();

		} catch (ClassNotFoundException cnfe) {

		} catch (NoSuchMethodException nsme) {

		} catch (InvocationTargetException ite) {

		} catch (IllegalAccessException iae) {

		}
	}
	
}
