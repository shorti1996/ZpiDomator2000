package zpi.pls.zpidominator2000;

import android.os.Bundle;

import com.ldealmei.libs.carousel.CarouselPicker;
import com.ldealmei.libs.carousel.ItemPicker;

import java.util.ArrayList;
import java.util.List;

import zpi.pls.AppCompatActivityWithBackButton;

public class AboutActivity extends AppCompatActivityWithBackButton {

    CarouselPicker carouselPicker1;
    CarouselPicker carouselPicker2;
    CarouselPicker carouselPicker3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        carouselPicker1 = findViewById(R.id.my_carousel_picker);
        carouselPicker2 = findViewById(R.id.my_carousel_picker1);
        carouselPicker3 = findViewById(R.id.my_carousel_picker2);

        List<ItemPicker> textItems = new ArrayList<>();
        textItems.add(new ItemPicker(R.drawable.thermometer, "Manage temperature in all house"));
        textItems.add(new ItemPicker(R.drawable.light, "Turn on/off lights"));
        textItems.add(new ItemPicker(R.drawable.flash, "See energy cost"));
        textItems.add(new  ItemPicker(R.drawable.increasingchart, "Analise your stats"));
        textItems.add(new ItemPicker(R.drawable.smartphone, "Policy per user"));
        textItems.add(new ItemPicker(R.drawable.smarthome, "Smart home"));
        textItems.add(new ItemPicker(R.drawable.power, "Manage your energy"));
        List<ItemPicker> textItems1 = new ArrayList<>();
        textItems1.add(new ItemPicker(R.drawable.piggybank, "Save money"));
        textItems1.add(new  ItemPicker(R.drawable.networkcloud, "Storage your data"));
        textItems1.add(new ItemPicker(R.drawable.wifi, "Your home network"));
        textItems1.add(new ItemPicker(R.drawable.wrench, "Support on call"));
        textItems1.add(new ItemPicker(R.drawable.idea, "Automatic light management"));
        textItems1.add(new ItemPicker(R.drawable.hourglass, "Statistic in time"));
        textItems1.add(new  ItemPicker(R.drawable.growth, "See your savings"));
        List<ItemPicker> textItems2 = new ArrayList<>();
        textItems2.add(new ItemPicker(R.drawable.bolt, "Save energy"));
        textItems2.add(new ItemPicker(R.drawable.gearwheelscouple, "Full system"));
        textItems2.add(new  ItemPicker(R.drawable.fan, "Manage cooling"));
        textItems2.add(new ItemPicker(R.drawable.group, "For families"));
        textItems2.add(new  ItemPicker(R.drawable.app_icon_2, "InHouse"));
        textItems2.add(new ItemPicker(R.drawable.speechbubbles, "Support 24h/7"));
        textItems2.add(new ItemPicker(R.drawable.electronic, "Fully functional solution"));
        carouselPicker1.addList(textItems).build(this);
        carouselPicker2.addList(textItems1).build(this);
        carouselPicker3.addList(textItems2).build(this);

        Utils.setupActionBar(this);
    }

}
