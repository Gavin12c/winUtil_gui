package com.hy.winUtil_gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
//import com.sun.jna.examples.win32.Kernel32;  
//import com.sun.jna.examples.win32.User32;  
//import com.sun.jna.examples.win32.User32.HHOOK;  
//import com.sun.jna.examples.win32.User32.MSG;  
//import com.sun.jna.examples.win32.W32API.HMODULE;  
//import com.sun.jna.examples.win32.W32API.LRESULT;  
//import com.sun.jna.examples.win32.W32API.WPARAM;  
//import com.sun.jna.examples.win32.User32.HOOKPROC;  
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinDef.LPARAM;

public class MouseHook implements Runnable{  
      
    public static final int WM_MOUSEMOVE = 512;
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_LBUTTONUP = 514;
    public static final int WM_RBUTTONDOWN = 516;
    public static final int WM_RBUTTONUP = 517;
    public static final int WM_MBUTTONDOWN = 519;
    public static final int WM_MBUTTONUP = 520;
    private static HHOOK hhk;   //钩子的句柄
    private static LowLevelMouseProc mouseHook;  //
    static User32 lib;   //window应用程序接口
    private boolean isWindows = false;
    private HMODULE hMod;
    private boolean [] on_off=null;  
  
    public MouseHook(boolean [] on_off){  
    	this.on_off = on_off;  
    	isWindows = Platform.isWindows();
    	if(isWindows){
    		lib = User32.INSTANCE;
    	}
    }  
  
    /**
     * 定义鼠标钩子,及事件监听回调
     */
    public interface LowLevelMouseProc extends HOOKPROC {
        LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);  
    }  
  
    public static class MOUSEHOOKSTRUCT extends Structure {  
        public static class ByReference extends MOUSEHOOKSTRUCT implements  
        Structure.ByReference {  
        };  
        public User32.POINT pt;  //点坐标
        public HWND hwnd;//窗口句柄
        public int wHitTestCode;  
        public User32.ULONG_PTR dwExtraInfo; //扩展信息
		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("dwExtraInfo","hwnd","pt","wHitTestCode");
		}  
    }  
  
    private boolean LB = false;
    private boolean MB = false;
    
    public void run() {
    	final ImageApp imageApp = new ImageApp();
    	hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        mouseHook = new LowLevelMouseProc() {  
            public LRESULT callback(int nCode, WPARAM wParam,  
                    MOUSEHOOKSTRUCT info) {  
                if (on_off[0] == false) {  
                    System.exit(0);  
                }  
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
	                    case MouseHook.WM_LBUTTONDOWN:
	                    	System.out.println("点击了左键");
	                    	break;
	                    case MouseHook.WM_RBUTTONDOWN:
	                    	System.out.println("点击了右键");
	                    	break;
	                }
                	if(wParam.intValue() == MouseHook.WM_MBUTTONDOWN){
                		MB = true;
                	}else if(wParam.intValue() == MouseHook.WM_MBUTTONUP){
                		MB = false;
                	}
                	
                	if(wParam.intValue() == MouseHook.WM_LBUTTONDOWN){
                		LB = true;
                	}else if(wParam.intValue() == MouseHook.WM_LBUTTONUP){
                		LB = false;
                	}
                	
                	if(LB && MB){
                		imageApp.setVisible(true);
                	}else{
                		imageApp.setVisible(false);
                	}
                	
                	
                }  
                Pointer pointer = info.getPointer();
                long peer = Pointer.nativeValue(pointer);
                return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));  
            }  
        };  
        hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);  
        int result;  
        MSG msg = new MSG();  
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {  
                System.err.println("error in get message");  
                break;  
            } else {  
                System.err.println("got message");  
                lib.TranslateMessage(msg);  
                lib.DispatchMessage(msg);  
            }  
        }
        lib.UnhookWindowsHookEx(hhk);  
    }  
}  