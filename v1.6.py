import http
import socket
import http.server
import socketserver
import threading
from plyer import notification
from PIL import ImageGrab
import time
import pyautogui
import win32api, win32con, win32gui 
import win32gui, win32process


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
    imx = pyautogui.screenshot()
    imx.save("tempcap.png")
    while True:
        data, addr=s.recvfrom(1024)
        sendip = addr[0] 
        sendport = addr[1] 
        if data.decode().lower() == 'plstes':
            print('可以链接')
            s.sendto("连接成功".encode(),(sendip,21222))
        if data.decode().lower() == 'shot':
            im = pyautogui.screenshot()
            im.save("tempcap.png")
            print('截图')
        if data.decode().lower() == 'shotwindows':
            handle =win32gui.GetForegroundWindow()
            print(handle)
            (x1, y1, x2, y2), handle = win32gui.GetWindowRect(handle),handle
            win32gui.SendMessage(handle, win32con.WM_SYSCOMMAND, win32con.SC_RESTORE, 0)
            grab_image = ImageGrab.grab((x1, y1, x2, y2))
            grab_image.save("tempcap.png")
            print('截取局部窗口')
        if data.decode().lower() == 'bye':
            break
    s.close( )


    

if __name__ == '__main__':
    t1 = threading.Thread(target=work1)
    t1.start()
    time.sleep(1)
    t2 = threading.Thread(target=work2)
    t2.start()






