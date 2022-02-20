import http
import socket
import http.server
import socketserver
import threading
import time
import win32api
import win32con
import win32gui
import win32ui
import winsound
import win32print
from PIL import Image


proportion=100

PORT = 8000
Handler = http.server.SimpleHTTPRequestHandler
s=socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
def work1():
    ip = socket.gethostbyname(socket.gethostname())
    with socketserver.TCPServer(("", PORT), Handler) as httpd:
        print("服务运行中，你的ip是",ip)
        httpd.serve_forever()


def work2():
    s.bind(('', 21211))
    window_capture("tempcap.png")
    while True:
        data, addr=s.recvfrom(1024)
        sendip = addr[0] 
        sendport = addr[1] 
        if data.decode().lower() == 'plstes':
            print('可以链接')
            s.sendto("abc".encode(),(sendip,21222))
            #noticesound(3)
        if data.decode().lower() == 'shot':
            #noticesound(2)
            window_capture("tempcap.png")
            print('截图')
        if data.decode().lower() == 'shotwindows':
            #noticesound(1)
            window_capturex("tempcap.png")
            print('截取局部窗口')
        if data.decode().lower() == 'bye':
            break
    s.close( )

def noticesound(a):
        if a==1:
            winsound.Beep(350, 100)
            winsound.Beep(100, 200)
        elif a==2:
            winsound.Beep(100, 100)
            winsound.Beep(350, 200)
        elif a==3:
            winsound.Beep(500, 100)
            winsound.Beep(250, 200)

    
def window_capture(filename):
  hwnd = 0 # 窗口的编号，0号表示当前活跃窗口
  # 根据窗口句柄获取窗口的设备上下文DC（Divice Context）
  hwndDC = win32gui.GetWindowDC(hwnd)
  # 根据窗口的DC获取mfcDC
  mfcDC = win32ui.CreateDCFromHandle(hwndDC)
  # mfcDC创建可兼容的DC
  saveDC = mfcDC.CreateCompatibleDC()
  # 创建bigmap准备保存图片
  saveBitMap = win32ui.CreateBitmap()
  # 获取监控器信息
  MoniterDev = win32api.EnumDisplayMonitors(None, None)
  w,h=get_real_resolution()
  # print w,h　　　#图片大小
  # 为bitmap开辟空间
  saveBitMap.CreateCompatibleBitmap(mfcDC, w, h)
  # 高度saveDC，将截图保存到saveBitmap中
  saveDC.SelectObject(saveBitMap)
  # 截取从左上角（0，0）长宽为（w，h）的图片
  saveDC.BitBlt((0, 0), (w, h), mfcDC, (0, 0), win32con.SRCCOPY)
  saveBitMap.SaveBitmapFile(saveDC, filename)


#局部截图
def window_capturex(filename):
  hDC = win32gui.GetDC(0)
  w = win32print.GetDeviceCaps(hDC, win32con.DESKTOPHORZRES)
  proportion = round(w/win32api.GetSystemMetrics(0), 2)
  print(proportion)
  hwnd_target = win32gui.GetForegroundWindow() # 窗口的编号，0号表示当前活跃窗口
  left, top, right, bot = win32gui.GetWindowRect(hwnd_target)
  left=int(left*proportion)
  top=int(top*proportion)
  right=int(right*proportion)
  bot=int(bot*proportion)
  w = right - left
  h = bot - top
  win32gui.SetForegroundWindow(hwnd_target)

  hdesktop = win32gui.GetDesktopWindow()
  hwndDC = win32gui.GetWindowDC(hdesktop)
  mfcDC  = win32ui.CreateDCFromHandle(hwndDC)
  saveDC = mfcDC.CreateCompatibleDC()
  saveBitMap = win32ui.CreateBitmap()
  saveBitMap.CreateCompatibleBitmap(mfcDC, w, h)
  saveDC.SelectObject(saveBitMap)
  result = saveDC.BitBlt((0, 0), (w, h), mfcDC, (left, top), win32con.SRCCOPY)
  bmpinfo = saveBitMap.GetInfo()
  bmpstr = saveBitMap.GetBitmapBits(True)
  im = Image.frombuffer(
    'RGB',
    (bmpinfo['bmWidth'], bmpinfo['bmHeight']),
    bmpstr, 'raw', 'BGRX', 0, 1)
  win32gui.DeleteObject(saveBitMap.GetHandle())
  saveDC.DeleteDC()
  mfcDC.DeleteDC()
  win32gui.ReleaseDC(hdesktop, hwndDC)
  if result == None:
    #PrintWindow Succeeded
    im.save("tempcap.png")


def get_real_resolution():
    """获取真实的分辨率"""
    hDC = win32gui.GetDC(0)
    # 横向分辨率
    w = win32print.GetDeviceCaps(hDC, win32con.DESKTOPHORZRES)
    # 纵向分辨率
    h = win32print.GetDeviceCaps(hDC, win32con.DESKTOPVERTRES)
    proportion = round(w/h, 2)
    return w, h




if __name__ == '__main__':
    t1 = threading.Thread(target=work1)
    t1.start()
    time.sleep(1)
    t2 = threading.Thread(target=work2)
    t2.start()






