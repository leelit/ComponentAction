S# ComponentAction
不管是Activity、Service还是BroadcastReceiver都是【全局唯一】的，比如你在一个本地进程开启一个Service，如果在另外一个远程进程开启同样一个Service，这个Service之前已经被创建，所以是不会回调onCreate方法，只会回调onStartCommand方法。系统Framework层是通过【包名】以及【类名】定位到这个唯一组件。

1. 广播接收者的接收回调方法始终运行在【所在进程】的【主线程】

2. Service的所有回调方法都始终运行在【所在进程】的【主线程】

3. 如果【Service运行在同一个进程】，则aidl调用运行在【调用者所在线程】，比如在主线程调用aidl#test()，则该方法运行在本进程的主线程，换言之如果Service运行在同一进程则并无【IPC】。

4. 如果Service运行在另外一个进程，则aidl调用运行在【Binder线程池】，比如在主线程调用aidl#test()，则该方法运行在远程进程的Binder线程池。

5. 不管是否在同一个进程，Service的aidl调用是同步调用，比如在Service在同一进程，主线程调用aidl#test()方法，该方法长时间运行，则有可能造成ANR。

6. Service被杀后，系统根据onStartCommand返回值会有不同的行为，比如重启这个Service，但是即便相同返回值在不同系统之间可能表现不太一样，所以client端还是要做好相关的判断，究竟该Service还活着不。

7. 启动任何一个远程进程，都会实例一个Application对象，所以继承Application的方法进行初始化操作时需要注意，比如一个A类在MyApplication#onCreate方法里进行初始化，如果该类完全不可能运行在另外一个远程进程时，就需要注意判断该类在哪个进程才需要初始化。
