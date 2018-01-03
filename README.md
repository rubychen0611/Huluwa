﻿# 葫芦娃大作业说明

#### 陈紫琦 151220017
---
## 效果演示及说明
游戏开始界面：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/1.png)

空格键开始游戏，葫芦娃和妖精分别以长蛇形和锋矢型站于左右两侧，其中葫芦娃的排列顺序随机：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/2.png)

双方开始交战，距离等于1单位，即妖精在葫芦娃前后左右时两者相遇，其中一人死亡，在该位置上留下尸体：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/3.png)

直到一方全部死亡时游戏停止，出现结束页面：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/4.png)

此时按键盘S键可保存记录至项目target目录下，出现提示框则保存成功：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/5.png)

保存文件扩展名为.dat，以保存时间来命名：

![](https://github.com/rubychen0611/Huluwa/raw/master/screenshots/6.png)

空格键返回初始界面，按键盘L键出现文件选择对话框，选择相应的.dat文件即可开始回放，精彩回放在根目录下的example.dat中。

## 代码说明
### 数据结构

&emsp;&emsp;Creature为所有生物的基类，由此派生出葫芦娃、妖精等各个子类，在子类的构造器中确定其具体的物种、阵营（好人/坏人）、战斗力值、正常图片和尸体图片等等。由于各个生物行为基本相同，因此setPosition()、getPosition()、die()、decideNextPos()等行为都在Creture类里做出了描述，Creture类还实现了Runnable接口，控制多线程时各个生物的行为。

&emsp;&emsp;抽象类Queue<T extends Creature>是一个泛型类，其子类HuluwaQueue和ScorpionQueue分别表示了葫芦娃的队列和妖精队列，Queue中主要确定了队列人数以及所用到的阵型。阵型基类为Formation，派生出子类LongSnakeForm和SwordForm代表长蛇形锋矢型阵型。

&emsp;&emsp;Space类表示整个战场的空间，并且由该类完成所有场景绘制、决定战斗输赢、多线程控制等功能，其中成员包含11*9个Position位置类对象的二维数组。Position类控制位置的管理及以及相关的线程协同问题，同时还保存了所有在该位置死亡的生物的编号以供绘制。

### 多线程

&emsp;&emsp;每个生物的线程由Creature类的run方法描述，主要循环是在未死亡且战斗未结束且线程未被打断的情况下，首先判断下一步的移动位置，如果位置上有人则等待，被唤醒后离开原来的位置，移动到新位置，然后随机sleep一段时间（800ms-1800ms）。其中判断下一步的算法比较简单，先获取当前战场的状况，如果前面有敌人下一步就向前，后面有敌人下一步就向后，如果前后都没有敌人，遍历所有敌人，找到最近的一个，判断其在自己的上方还是下方来决定下一步向上或者向下。

&emsp;&emsp;所有线程的管理由Space类控制，采用的是线程池的创建和管理方法，创建好每个生物的线程并启动后，用λ表达式建立一个刷新屏幕的线程，每次刷新后sleep 200ms，然后遍历所有生物判断有没有战斗情况出现，即如果找到两个相邻的敌对生物，确定双方输赢，然后调用die()让其中一个死亡，并且该阵营人数减一，直到一方人数为0设置标志位ifBattleEnds为true。

&emsp;&emsp;确定输赢的算法也比较简单，首先每个生物的战斗力值如下（越高表示越强）：葫芦娃中大娃为8，依次递减，七娃为2，老爷爷也为2，妖精阵营中蛇精为8，蝎子精为7，所有小喽啰都是1。每当敌对生物相遇，假设二者中战斗力高的生物为a，低的为b，则a赢的概率p为（50 + （a的战斗力-b的战斗力）*5）%，获取一个1-100之间的随机数r，如果r<=p，则a赢，否则b赢。这样设置的话，如果二人战斗力相同的情况下，二人赢的概率各50%，战斗力最悬殊的情况下（8和1相遇），强者赢的概率为85%。这样每次强者赢的概率在50%-85%之间，保证了游戏的随机性，不会出现一方总是赢的情况。

&emsp;&emsp;多线程的协同是一个比较困扰我的问题，最初以为只要控制好Position的同步就好了，但在Position类的相关方法加上synchronized后，总是会有各种异常出现。因此我很迷惑，不知道到底应该在哪些地方需要控制多线程的冲突，直到在《Thinking in Java》里找到一句话，大概意思是：如果你正在写一个变量，他可能接下来被另一个线程读取，或者正在读取一个上一次已经被另一个线程写过的变量，那你就必须使用同步，并且读写线程都应该用相同的监视器锁同步。因此我仔细找出了程序中所有这种变量，除了position外，还有各个生物的状态（是否活着），Space类中的ifBattleEnds(比赛是否结束)、双方阵营的剩余人数等，因此在这些变量的相关方法上也加上了synchronized修饰。然而不幸的是仍然有异常情况出现，经过排查和调试，发现很多方法不仅得在方法上上锁，在调用其的方法内部也得使用synchronized同步，比如每个生物移动的时候先离开位置，再进入新位置，多线程在这两句话之间切换，此时该生物的position变量会变成null，因此必须也使用同步。但synchronized如果滥用的话又会产生死锁，画面卡住不动，改变每个线程的名字，利用java相关的线程查看命令进行调试，查看每个线程在哪一步停止，终于解决了线程问题。

&emsp;&emsp;在添加游戏可以返回从头开始的功能后，又出现了一个问题即画面上可能同时出现两个相同的葫芦娃。经过判断发现是上一轮游戏的进程未完全结束，虽然设置了ifBattleEnds和线程的interrupted，但该线程的最后一轮循环可能还未完成，因此在战斗结束后设置等待1s（sleep(1000)）来等待所有线程结束。
### 回放
&emsp;&emsp;添加一个package Replay用于管理回放及文件的类，首先添加类Record表示一次游戏记录，其成员为一个ArrayList<Scene>的数组来保存此次游戏每隔200ms的所有场景，场景Scene类中又有一个数组ArrayList<DisplayElement>用于该场景中的所有显示元素，包括所有活着的生物以及所有死了的生物的尸体，其中显示元素DisplayElement类具有四个成员——生物编号，是否已死亡，x轴坐标，y轴坐标。

&emsp;&emsp;定义好记录的格式后，添加一个类FileManager用于对文件和记录的管理，它的行为主要是在用户选择保存后，写文件，用户选择回放后读文件。

&emsp;&emsp;文件输入输出采用面向字节的流形式，输入输出为二进制文件（.dat），写入的内容首先为record中的场景数和战斗结果，然后对每个场景循环，写入场景中的显示元素数，以及各个显示元素的属性。读出时同理，最后读入一个record对象中。再新建一个线程每隔150ms进行回放。如果设为和第一次生成时一样sleep 200ms，效果上看似乎有点慢，没有太想明白为什么，因此设为150ms，效果较为正常。

&emsp;&emsp;为了减少读取图片所需时间，回放前将每个生物活着的图片和尸体图片都存入map中，以每个生物唯一的生物编号进行存取，生物编号在对象创建时由Creature类中的一个static成员count获得。

### 用到的面向对象思想
- 抽象  
&emsp;&emsp;将场景中不同的对象归类，最终抽象为生物体、生物体队列、阵型等几个基类或接口，再分别从基类派生出不同的子类，反映了对象所具备的重要性质，使程序模块和结构更清晰。
- 继承  
&emsp;&emsp;多次使用到继承机制，如将Queue由原本的类改为基类，派生出HuluwaQueue类和ScorpionQueue类，不仅对象所具备的特性（相同点与差异）显得更为清晰，更大大减少了代码重复量。
- 封装  
&emsp;&emsp;过程封装与数据封装，如各个类中的成员都以private或protected修饰，只能通过get、set方法获取和修改，更好地保护了数据。
### 用到的设计原则和模式
- 里式替换法则

  父类（如Creaure）出现的地方，子类(如各个生物子类)可以代入。
- 单一职责原则

让一个类只做一种类型责任。如单独建立FileManager类管理文件。
- 合成聚合复用原则

优先使用对象合成/聚合，而不是继承。
- 包装器模式

装饰模式即给一个对象增加一些新的功能。如文件输入时采用了从先从文件读数据至缓冲流，再从缓冲中读取数据。
```java
DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
```
- 观察者模式

定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。如键盘状态的监视。
- 状态模式

当对象的状态改变时，同时改变其行为。如战场类共有5种状态，开始、战斗中、结束、回放和回放结束（回放结束和普通结束不同是因为回放之后不能保存)，根据状态改变绘图。

## 总结
&emsp;&emsp;通过这学期的java学习和葫芦娃的大作业，我对面向对象的编程思想有了更深刻的认识，同时还了解了很多设计的方法和原则。不足是葫芦娃里用到的不多，并且由于时间关系来不及使代码更加规范和完善。比如多态，在图形化之前各个生物还可以通过报名字实现多态，但图形化之后则没有必要了。总之我会在以后编程时更加多地利用这学期java课所学到的思想。





