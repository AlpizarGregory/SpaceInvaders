package sample;

import java.util.ArrayList;
import java.util.List;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
//hola

public class Main extends Application {

    AnimationTimer timer;
    Pane root = new Pane();
    List<ImageView> monsters = new ArrayList<>();
    List<Circle> mShoots = new ArrayList<>();
    List<Circle> pShoots = new ArrayList<>();
    ImageView player;
    Circle dotR = new Circle();
    Boolean toRight = true;
    Text lives;
    Text points;
    int numPoins = 0;
    int numLives = 3;
    double  mousex;



    public static void main(String[] args) {



        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {




        //Life and Point
        lives = new Text("Lives: 3");
        lives.setLayoutX(20);
        lives.setLayoutY(30);
        lives.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        lives.setFill(Color.WHITE);
        points = new Text("Points: 0");
        points.setLayoutX(350);
        points.setLayoutY(30);
        points.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        points.setFill(Color.WHITE);
        root.getChildren().addAll(lives, points);

        //dot that regulates moving of monsters
        dotR.setLayoutX(0);


        //Creating player
        player = player();
        root.getChildren().add(player);

        //Create Monsters
        addMonsters();

        //Animation timer
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameUpdate();
            }
        };
        timer.start();

        //Timeline for making monster shoots every few seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (!monsters.isEmpty()) {
                monstersShoot();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //Setting up Stage
        Scene scene = new Scene(root, 500, 700);
        scene.setFill(Color.BLACK);

        //moving player



        scene.setCursor(Cursor.MOVE);
        scene.setOnMouseMoved( e -> {
            mousex = e.getX();
            player.setLayoutX(mousex);

        });

        scene.setOnMouseClicked( e ->{
            if (e.getButton() == MouseButton.PRIMARY) {
                playerShoot(player.getLayoutX());
            }
        });



        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();

        MosterList list1 = new MosterList<>();
        list1.add(1);
        System.out.println(list1.get(0));


    }


    public void gameUpdate() {
        monstersShootUpdate();
        playersShootUpdate();
        isPlayerDestroyed();
        isMonsterDestroyed();
        monstersMove();
        isWin();
        isLost();
    }

    public int rand(int min, int max) {
        return (int)(Math.random() * max + min);}




    public void addMonsters(){
        for(int i = 0, w = 40; i < 6; i++, w+= 70 ){
            monsters.add(monster(w,50));
            root.getChildren().add(monsters.get(i));}

    }



    public void monstersMove(){
        //monsters moving
        double speed;

        if(toRight)
            speed = 0.7;
        else
            speed = -0.7;

        if(dotR.getLayoutX() >= 40){
            toRight = false;
            for (int i = 0; i < monsters.size(); i++){
                monsters.get(i).setLayoutY(monsters.get(i).getLayoutY() + 8);
            }
        }

        if(dotR.getLayoutX() <= -20){
            toRight = true;
            for (int i = 0; i < monsters.size(); i++){
                monsters.get(i).setLayoutY(monsters.get(i).getLayoutY() + 8);
            }
        }

        for (int i = 0; i < monsters.size(); i++){
            monsters.get(i).setLayoutX(monsters.get(i).getLayoutX() + speed);
        }
        dotR.setLayoutX(dotR.getLayoutX() + speed);
    }

    private void monstersShoot(){
        int getShootingMonsterIndex = rand(0, monsters.size() - 1);
        mShoots.add(shoot(monsters.get(getShootingMonsterIndex).getLayoutX() + 25, monsters.get(getShootingMonsterIndex).getLayoutY() + 25));
        root.getChildren().add((Node) mShoots.get(mShoots.size() - 1));
    }

    private void monstersShootUpdate(){
        if(!mShoots.isEmpty()){
            for (int i = 0; i < mShoots.size(); i++){
                mShoots.get(i).setLayoutY(mShoots.get(i).getLayoutY() + 3);
                if (mShoots.get(i).getLayoutY()<= 0){
                    root.getChildren().remove(mShoots.get(i));
                    mShoots.remove(i);
                }
            }
        }
    }

    private void isMonsterDestroyed(){
        for (int i = 0; i < pShoots.size(); i++){
            for (int j = 0; j < monsters.size(); j++){
                if (       ((pShoots.get(i).getLayoutX() > monsters.get(j).getLayoutX())
                        && ((pShoots.get(i).getLayoutX() < monsters.get(j).getLayoutX() + 50))
                        && ((pShoots.get(i).getLayoutY() > monsters.get(j).getLayoutY())
                        && ((pShoots.get(i).getLayoutY() < monsters.get(j).getLayoutY() + 50))))){
                    root.getChildren().remove(monsters.get(j));
                    monsters.remove(j);
                    root.getChildren().remove(pShoots.get(i));
                    pShoots.remove(i);
                    numPoins += 10;
                    points.setText("Points: " + String.valueOf(numPoins));

                }
            }
        }
    }

    public ImageView player(){
        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Player.png")));
        i.setLayoutX(225);
        i.setLayoutY(650);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }

    public ImageView monster(double x, double y){
        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Invader.png")));
        i.setLayoutX(x);
        i.setLayoutY(y);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }

    public void playerShoot(double x){
        pShoots.add(shoot(player.getLayoutX() + 25, player.getLayoutY() +25));
        root.getChildren().add(pShoots.get(pShoots.size() - 1));
    }

    public void playersShootUpdate(){
        if (!pShoots.isEmpty()){
            for (int i = 0; i < pShoots.size(); i++){
                pShoots.get(i).setLayoutY(pShoots.get(i).getLayoutY() - 3);
                if (pShoots.get(i).getLayoutY() <= 0){
                    root.getChildren().remove(pShoots.get(i));
                    pShoots.remove(i);
                }
            }
        }
    }

    private void isPlayerDestroyed(){
        for (int i = 0; i < mShoots.size(); i++){
            if (      ((mShoots.get(i).getLayoutX() > player.getLayoutX())
                    &&((mShoots.get(i).getLayoutX() < player.getLayoutX() + 50))
                    &&((mShoots.get(i).getLayoutY() > player.getLayoutY())
                    &&((mShoots.get(i).getLayoutY() < player.getLayoutY() + 50))))){
                player.setLayoutX(225);
                numLives -= 1;
                lives.setText("Lives: "+ String.valueOf(numLives));
            }
        }
    }

    public Circle shoot(double x, double y){
        Circle c = new Circle();
        c.setFill(Color.GREENYELLOW);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setRadius(3);
        return c;
    }

     public void isWin(){
        if(monsters.isEmpty()){
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            text.setX(80);
            text.setY(300);
            text.setFill(Color.YELLOW);
            text.setStrokeWidth(3);
            text.setStroke(Color.CRIMSON);
            text.setText("WIN & Points: " + numPoins);
            root.getChildren().add(text);
            timer.stop();
        }
     }

     public void isLost(){
        if(numLives <= 0){
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            text.setX(80);
            text.setY(300);
            text.setFill(Color.YELLOW);
            text.setStrokeWidth(3);
            text.setStroke(Color.CRIMSON);
            text.setText("LOST & Points: " + numPoins);
            root.getChildren().add(text);
            timer.stop();

        }
     }
}



