## ComponentAction
0. 不管是Activity、Service还是BroadcastReceiver都是【全局唯一】的，比如你在一个A进程开启一个Service，如果在另外一个B进程开启同样一个Service，这个Service之前已经被创建，所以B进程是不会回调onCreate方法，只会回调onStartCommand方法。而系统Framework层是通过【包名】以及【类名】定位到这个唯一组件。

1. 广播接收者的接收回调方法始终运行在【所在进程】的【主线程】

2. Service的所有回调方法都始终运行在【所在进程】的【主线程】

3. 如果【Service运行在同一个进程】，则aidl调用运行在【调用者所在线程】。比如在A进程的主线程调用aidl#test()，则该方法运行在A进程的主线程，换言之如果Service运行在【同一进程则并无IPC】，所有调用都是在当前线程。

4. 如果Service运行在另外一个进程，则aidl调用运行在【Binder线程】。比如Service运行在B进程，在A进程的主线程调用aidl#test()，则该方法运行在B进程的Binder线程。

5. 不管是否在同一个进程，Service的aidl调用都是【同步调用】。

6. 当A进程aidl调用B进程的过程中发生回调，如果B进程是在Binder线程进行回调，则回调方法运行在A进程aidl调用时所在线程，否则运行在A进程的Binder线程。比如A进程在主线程调用B进程的test方法，test方法运行在B进程的Binder线程，如果test方法中又通过RemoteCallBackList回调A进程，则回调方法运行在A进程的主线程；如果B进程回调时是在子线程，则回调方法运行在A进程的Binder线程。

7. Service被杀后，系统根据onStartCommand返回值会有不同的行为，比如重启这个Service，但是即便相同返回值在不同系统之间可能表现不太一样，所以client端还是要做好相关的判断，究竟该Service还活着不。

8. 启动任何一个远程进程，都会实例一个Application对象，所以继承Application的方法进行初始化操作时需要注意，比如一个A类在MyApplication#onCreate方法里进行初始化，如果该类完全不可能运行在另外一个远程进程时，就需要注意判断该类在哪个进程才需要初始化。
