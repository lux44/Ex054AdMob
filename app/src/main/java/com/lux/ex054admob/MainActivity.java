package com.lux.ex054admob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    //모바일 광고 플랫폼 : AdMob [Google] - https://admob.google.com/
    //1)구글계정으로 가입 및 로그인
    //2)앱 등록 및 광고단위 Id 제작

    //엡은 우선 Test Sample Unit Id로 먼저 제작 한 후에 Id만 변경하면 됨.

    //AdMob SDK 외부라이브러리를 이 프로젝트에 적용하기(다운&연결)
    //가이드 문서를 참조하여 제작 - https://developers.google.com/admob
    //Gradle 빌드 프로그램이 라이브러리 적용 작업을 손쉽게 해줌.

    //** Android Studio BumbleBee 버전의 변경사항 **
    //프로젝트 수준의 build.gradle에 하던 작업을 settings.gradle에서 작업함.


    //[1. Banner 광고 view 참조변수]
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //모바일 광고에 대한 SDK 초기화
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                //광고 초기화가 완료되면 실행되는 영역메소드
            }
        });

        //배너 광고 뷰 참조하기
        adView=findViewById(R.id.adView);

        //광고 로드하기
        AdRequest adRequest=new AdRequest.Builder().build(); //광고 요청 객체
        adView.loadAd(adRequest);

        //[2] 전면광고 보여주기
        //버튼 클릭시에 전면광고 보여주기
        findViewById(R.id.btn).setOnClickListener(view -> {
            showInterstitialAd();
        });

        //[3]보상형 광고 보여주기 [람다식의 실행문이 1줄이면 {}도 생략가능, 단, ;도 생략해야 함]
        findViewById(R.id.btn2).setOnClickListener(view ->  showRewardedAd());

        //[4] 네이티브 광고 [ 원하는 모양으로 광고 모양 레이아웃을 설계 ]
        //하지만 가이드 문서가 온전하지 않아서 지금은 수업 보류
    }

    //[2. 전면광고 참조변수]
    private InterstitialAd interstitialAd;


    //전면광고를 보여주는 기능메소드
    void showInterstitialAd(){
        //sample UnitId : ca-app-pub-3940256099942544/1033173712

        //광고요청 객체
        AdRequest adRequest= new AdRequest.Builder().build();

        //전면광고 로드하기
        InterstitialAd.load(this, "ca-app-pub-7528229371916709/9659872177", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                //광고 로딩에 실패했을 때 자동으로 발동하는 콜백메소드
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(MainActivity.this, "광고 로딩 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                //광고 로딩에 성공했을 때 자동으로 발동하는 콜백메소드
                super.onAdLoaded(interstitialAd);
                //성공했을때 파라미터로 전면광고 객체가 전달됨

                //전면광고 객체를 멤버변수에 대입해주기 - 다른 버튼 눌렀을때도 광고 보여주기 등의 작업이 가능함.
                MainActivity.this.interstitialAd= interstitialAd;

                //전면광고 보이기
                interstitialAd.show(MainActivity.this);
            }
        });

    }
    //3.보상형 광고 참조변수
    RewardedAd rewardedAd;

    //보상형 광고를 보여주는 기능메소드
    void showRewardedAd(){
        //sample ID : ca-app-pub-3940256099942544/5224354917

        //광고 요청 객체 생성
        AdRequest adRequest=new AdRequest.Builder().build();

        //보상형 광고 로딩하기
        RewardedAd.load(this, "ca-app-pub-7528229371916709/5177701683", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Toast.makeText(MainActivity.this, "보상 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                //멤버변수에 보상형광고 객체 참조시키기
                MainActivity.this.rewardedAd = rewardedAd;

                //보상형 광고 보기 - 사용자가 일정시간 이상 광고동영상을 봄으로써 이득(즉, 보상)을 얻었을때 리스너 처리
                rewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        //파라미터(rewardItem) : 기준 시간 이상 광고를 시청하면 주어지는 보상 아이템
                        //                   [ 개발자가 광고단위 ID를 만들때 설정한 값 ] - 샘플 ID로 실행하면 type : "coins", amount : 10
                        String type = rewardItem.getType(); //상품명
                        int amount = rewardItem.getAmount(); //수량

                        Toast.makeText(MainActivity.this, type+": " +amount +"", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}