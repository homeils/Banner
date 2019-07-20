# Banner - 轮播
>> 使用ViewPager制作的轮播。支持设置图片加载器，设置indicator圆点大小，设置自动轮播间隔时间，设置图片集合，设置标题集合以及标题字体颜色。目前indicator
的位置是固定在左下角的，字体大小及格式也是默认的（个人觉得这样看着比较舒服，就懒得抛出设置，有兴趣的可以参考源码自行修改）。布局是采用Constraintlayout
约束布局，特别好用的布局，极力推荐，没有引入约束布局依赖的记得加依赖。欢迎大家使用以及提出优化意见或Bug！
## 向项目中引入依赖
>> 在Project下的gradle中添加jitpack仓库依赖：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
>> 在app下的gradle中添加如下依赖：
```
dependencies {
    ...
    implementation 'com.github.renoside:Banner:v2.1.1'
    ...
}
```
## 如何使用
>> 布局文件示例：
```
<com.renoside.banner.Banner
    android:id="@+id/test_banner"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintDimensionRatio="3 : 2"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```
>> java代码中设置：
```
banner.setImageLoader(new ImageLoader() {
    @Override
    public void conImageLoader(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}); //设置图片加载器，例举Glide，可自行选择
banner.setPointSize(20);  //设置indicator圆点大小，单位dp
banner.setRelay(2000);  //设置轮播间隔时长，单位毫秒
banner.setTitleColor(0xFFFFFFFF); //设置标题字体颜色，0x八位十六进制（前两位透明度，后六位颜色）
banner.setImages(images); //设置图片集合
banner.setTitles(titles); //设置标题集合，注意是List<String>
banner.start(); //启动轮播
banner.setOnBannerListener(new OnBannerListener() {
    @Override
    public void onClick(int position) {
        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}); //设置监听
```
## 说明
>> 项目有许多不足之处，还望多多包涵           
>> 作者：renoside     
>> Github主页：https://github.com/renoside          
