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

public class Main extends Application {

    AnimationTimer timer;
    Pane root = new Pane();
    MonsterList<ImageView> monsters = new MonsterList<>();
    DoubleMonsterList<ImageView> monstersDouble = new DoubleMonsterList<>();
    CircularMonsterList<ImageView> monstersCircular =  new CircularMonsterList<>();
    DoubleCircularMonsterList<ImageView> monstersDoubleCircular =  new DoubleCircularMonsterList<>();
    List<Circle> mShoots = new ArrayList<>();
    List<Circle> pShoots = new ArrayList<>();
    ImageView player;
    Circle dotR = new Circle();
    Boolean toRight = true;
    Text lives;
    Text points;
    Text level;
    Text wave;
    Text waveNext;
    int numPoints = 0;
    int numLives = 3;
    int numLevel = 0;
    int bossPosition;
    int bosslives;
    int waveType;
    int A[] = new int[10];
    int counter = 0;
    int firstAdd = 1;
    int counterWaveB = 0;
    int live1;
    int live2;
    int live3;
    int live4;
    int live5;
    int enemyPosition0;
    int enemyPosition1;
    int enemyPosition2;
    int enemyPosition4;
    int enemyPosition5;
    double mousex;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        //Life, level and Point
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
        level = new Text("Level: 1");
        level.setLayoutX(350);
        level.setLayoutY(60);
        level.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        level.setFill(Color.WHITE);
        wave = new Text(" - ");
        wave.setLayoutX(20);
        wave.setLayoutY(60);
        wave.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        wave.setFill(Color.WHITE);
        waveNext = new Text(" - ");
        waveNext.setLayoutX(20);
        waveNext.setLayoutY(85);
        waveNext.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        waveNext.setFill(Color.WHITE);
        root.getChildren().addAll(lives, points, level, wave, waveNext);


        //dot that regulates moving of monsters
        dotR.setLayoutX(0);


        //Creating player
        player = player();
        root.getChildren().add(player);

        //Create the random the enemy types
        setRandomList();


        //Create Monsters
        if (firstAdd == 1) {
            addMonsters();
            firstAdd++;
        } else {
            isWin();
        }


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
            if (monsters.size() != 0 || monstersDouble.size() != 0 || monstersCircular.size() != 0) {
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
        scene.setOnMouseMoved(e -> {
            mousex = e.getX();
            player.setLayoutX(mousex);

        });

        scene.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                playerShoot(player.getLayoutX());
            }
        });


        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();


    }


    public void gameUpdate() {
        monstersShootUpdate();
        playersShootUpdate();
        isPlayerDestroyed();
        isMonsterDestroyed();
        monstersMove();
        monsterWaveB();
        isWin();
        isLost();
    }

    public int rand(int min, int max) {
        return (int) (Math.random() * max + min);
    }


    public void setRandomList() {
        for (int i = 0; i <= 9; i++) {
            //A[i] = (int) (rand(1, 6));
            A[i] = 6;

        }
    }

    public void addMonsters() {

        waveType = A[counter];


        wave.setText("Wave type: " + String.valueOf(A[counter]));

        waveNext.setText("Next wave type: " + String.valueOf(A[counter + 1]));


        numLevel++;
        level.setText("Level: " + String.valueOf(numLevel));


        if (waveType == 1) {

            for (int i = 0, w = 46; i < 6; i++, w += 70) {
                monsters.add(monster(w, 80));
                root.getChildren().add(monsters.get(i));
            }
        }


        if (waveType == 2) {

            bossPosition = rand(0, 5);
            for (int i = 0, w = 46; i < 6; i++, w += 70) {
                if (i == bossPosition) {
                    monsters.add(monsterBoss(w, 80));
                } else {
                    monsters.add(monster(w, 80));
                }
                root.getChildren().add(monsters.get(i));
            }
        }

        if (waveType == 3) {

            bossPosition = rand(0, 5);
            for (int i = 0, w = 46; i < 6; i++, w += 70) {
                if (i == bossPosition) {
                    monstersDouble.add(monsterBoss(w, 80));
                } else {
                    monstersDouble.add(monster(w, 80));
                }
                root.getChildren().add(monstersDouble.get(i));
            }

        }

        if (waveType == 4) {

            bossPosition = rand(0, 5);
            for (int i = 0, w = 46; i < 6; i++, w += 70) {
                if (i == bossPosition) {
                    monstersCircular.add(monsterBoss(w, 80));
                } else {
                    monstersCircular.add(monster(w, 80));
                }
                root.getChildren().add(monstersCircular.get(i));
            }

        }

        if (waveType == 5) {

            bossPosition = rand(0,5);

/*

            live1 = 1;
            live2 = 2;
            live3 = 3;
            live4 = 5;
            live5 = 6;
            enemyPosition0 = 0;
            enemyPosition1 = 1;
            enemyPosition2 = 2;
            enemyPosition4 = 4;
            enemyPosition5 = 5;
*/
            for (int i = 0, w = 46; i < 6; i++, w += 70) {
                if (i == bossPosition) {
                    monstersCircular.add(monsterBoss(w, 80));
                }
                else {
                    monstersCircular.add(monster(w, 80));
                }
                root.getChildren().add(monstersCircular.get(i));
                monstersCircular.get(0);
            }
        }

        if(waveType==6){

            bossPosition = 2;
            for (int i = 0, w = 75; i < 5; i++, w += 70) {
                if (i == bossPosition) {
                    monstersDouble.add(monsterBoss(w, 80));
                } else {
                    monstersDouble.add(monster(w, 80));
                }
                root.getChildren().add(monstersDouble.get(i));
            }


        }

    }


    public void monstersMove() {
        //monsters moving
        double speed;


        if (waveType == 1 || waveType == 2) {
            if (toRight)
                speed = numLevel / 2.5;
            else
                speed = -numLevel / 2.5;

            if (dotR.getLayoutX() >= 40) {
                toRight = false;
                for (int i = 0; i < monsters.size(); i++) {
                    monsters.get(i).setLayoutY(monsters.get(i).getLayoutY() + 8);
                }
            }

            if (dotR.getLayoutX() <= -20) {
                toRight = true;
                for (int i = 0; i < monsters.size(); i++) {
                    monsters.get(i).setLayoutY(monsters.get(i).getLayoutY() + 8);
                }
            }

            for (int i = 0; i < monsters.size(); i++) {
                monsters.get(i).setLayoutX(monsters.get(i).getLayoutX() + speed);
            }
            dotR.setLayoutX(dotR.getLayoutX() + speed);
        }


        if (waveType == 3) {
            if (toRight)
                speed = numLevel / 2.5;
            else
                speed = -numLevel / 2.5;

            if (dotR.getLayoutX() >= 40) {
                toRight = false;
                for (int i = 0; i < monstersDouble.size(); i++) {
                    monstersDouble.get(i).setLayoutY(monstersDouble.get(i).getLayoutY() + 8);
                }
            }

            if (dotR.getLayoutX() <= -20) {
                toRight = true;
                for (int i = 0; i < monstersDouble.size(); i++) {
                    monstersDouble.get(i).setLayoutY(monstersDouble.get(i).getLayoutY() + 8);
                }
            }

            for (int i = 0; i < monstersDouble.size(); i++) {
                monstersDouble.get(i).setLayoutX(monstersDouble.get(i).getLayoutX() + speed);
            }
            dotR.setLayoutX(dotR.getLayoutX() + speed);
        }

        if (waveType == 4 || waveType == 5) {
            if (toRight)
                speed = numLevel / 2.5;
            else
                speed = -numLevel / 2.5;

            if (dotR.getLayoutX() >= 40) {
                toRight = false;
                for (int i = 0; i < monstersCircular.size(); i++) {
                    monstersCircular.get(i).setLayoutY(monstersCircular.get(i).getLayoutY() + 8);
                }
            }

            if (dotR.getLayoutX() <= -20) {
                toRight = true;
                for (int i = 0; i < monstersCircular.size(); i++) {
                    monstersCircular.get(i).setLayoutY(monstersCircular.get(i).getLayoutY() + 8);
                }
            }

            for (int i = 0; i < monstersCircular.size(); i++) {
                monstersCircular.get(i).setLayoutX(monstersCircular.get(i).getLayoutX() + speed);
            }
            dotR.setLayoutX(dotR.getLayoutX() + speed);
        }




        if (waveType == 6) {
            if (toRight)
                speed = numLevel / 2.5;
            else
                speed = -numLevel / 2.5;

            for (int i = 0; i < monstersDouble.size(); i++) {
                monstersDouble.get(i).setLayoutY(monstersDouble.get(i).getLayoutY() + speed);
            }
            dotR.setLayoutY(dotR.getLayoutY() + speed);

            if((monstersDouble.get(1).getLayoutX() >= 145 && monstersDouble.get(1).getLayoutX() <= 220) && monstersDouble.get(1).getLayoutY() <= monstersDouble.get(bossPosition).getLayoutY()){

                monstersDouble.get(1).setLayoutX(monstersDouble.get(1).getLayoutX() + speed);
                monstersDouble.get(1).setLayoutY(monstersDouble.get(1).getLayoutY() - speed);
                monstersDouble.get(0).setLayoutX(monstersDouble.get(0).getLayoutX() + speed + 0.4);
                monstersDouble.get(0).setLayoutY(monstersDouble.get(0).getLayoutY() - speed - 0.4);

                monstersDouble.get(3).setLayoutX(monstersDouble.get(3).getLayoutX() - speed );
                monstersDouble.get(3).setLayoutY(monstersDouble.get(3).getLayoutY() + speed );
                monstersDouble.get(4).setLayoutX(monstersDouble.get(4).getLayoutX() - speed - 0.4 );
                monstersDouble.get(4).setLayoutY(monstersDouble.get(4).getLayoutY() + speed + 0.4);



            }
            else if(monstersDouble.get(1).getLayoutX() >= 220  && monstersDouble.get(1).getLayoutY() < 230){
                monstersDouble.get(1).setLayoutX(monstersDouble.get(1).getLayoutX() + speed);
                monstersDouble.get(1).setLayoutY(monstersDouble.get(1).getLayoutY() + speed);
                monstersDouble.get(0).setLayoutX(monstersDouble.get(0).getLayoutX() + speed + 0.4);
                monstersDouble.get(0).setLayoutY(monstersDouble.get(0).getLayoutY() + speed + 0.4);

                monstersDouble.get(3).setLayoutX(monstersDouble.get(3).getLayoutX() - speed );
                monstersDouble.get(3).setLayoutY(monstersDouble.get(3).getLayoutY() - speed );
                monstersDouble.get(4).setLayoutX(monstersDouble.get(4).getLayoutX() - speed - 0.4);
                monstersDouble.get(4).setLayoutY(monstersDouble.get(4).getLayoutY() - speed - 0.4);

            }
            else if(monstersDouble.get(1).getLayoutX() >= 220  && monstersDouble.get(1).getLayoutY() > 230){
                monstersDouble.get(1).setLayoutX(monstersDouble.get(1).getLayoutX() - speed);
                monstersDouble.get(1).setLayoutY(monstersDouble.get(1).getLayoutY() + speed);
                monstersDouble.get(0).setLayoutX(monstersDouble.get(0).getLayoutX() - speed - 0.4);
                monstersDouble.get(0).setLayoutY(monstersDouble.get(0).getLayoutY() + speed + 0.4);

                monstersDouble.get(3).setLayoutX(monstersDouble.get(3).getLayoutX() + speed );
                monstersDouble.get(3).setLayoutY(monstersDouble.get(3).getLayoutY() - speed );
                monstersDouble.get(4).setLayoutX(monstersDouble.get(4).getLayoutX() + speed + 0.4);
                monstersDouble.get(4).setLayoutY(monstersDouble.get(4).getLayoutY() - speed - 0.4);

            }
            else if((monstersDouble.get(1).getLayoutX() >= 144 && monstersDouble.get(1).getLayoutX() <= 220) && monstersDouble.get(1).getLayoutY() >= monstersDouble.get(bossPosition).getLayoutY()){
                monstersDouble.get(1).setLayoutX(monstersDouble.get(1).getLayoutX() - speed);
                monstersDouble.get(1).setLayoutY(monstersDouble.get(1).getLayoutY() - speed);
                monstersDouble.get(0).setLayoutX(monstersDouble.get(0).getLayoutX() - speed - 0.4);
                monstersDouble.get(0).setLayoutY(monstersDouble.get(0).getLayoutY() - speed - 0.4);

                monstersDouble.get(3).setLayoutX(monstersDouble.get(3).getLayoutX() + speed );
                monstersDouble.get(3).setLayoutY(monstersDouble.get(3).getLayoutY() + speed );
                monstersDouble.get(4).setLayoutX(monstersDouble.get(4).getLayoutX() + speed + 0.4);
                monstersDouble.get(4).setLayoutY(monstersDouble.get(4).getLayoutY() + speed + 0.4);

            }

        }

    }

    public void monsterWaveB(){

        if(waveType == 3) {

            counterWaveB++;

            int change = rand(0, monstersDouble.size());

            if (counterWaveB % 175 == 0) {

                double newX = monstersDouble.get(change).getLayoutX();
                double newY = monstersDouble.get(change).getLayoutY();

                double oldX = monstersDouble.get(bossPosition).getLayoutX();
                double oldY = monstersDouble.get(bossPosition).getLayoutY();

                monstersDouble.get(bossPosition).setLayoutX(newX);
                monstersDouble.get(bossPosition).setLayoutY(newY);

                monstersDouble.get(change).setLayoutX(oldX);
                monstersDouble.get(change).setLayoutY(oldY);

            }
        }
    }








    private void monstersShoot() {
        if (waveType == 1 || waveType == 2) {
            int getShootingMonsterIndex = rand(0, monsters.size() - 1);
            mShoots.add(shootMonster(monsters.get(getShootingMonsterIndex).getLayoutX() + 25, monsters.get(getShootingMonsterIndex).getLayoutY() + 25));
            root.getChildren().add((Node) mShoots.get(mShoots.size() - 1));
        }


        if (waveType == 3) {
            int getShootingMonsterIndex = rand(0, monstersDouble.size() - 1);
            mShoots.add(shootMonster(monstersDouble.get(getShootingMonsterIndex).getLayoutX() + 25, monstersDouble.get(getShootingMonsterIndex).getLayoutY() + 25));
            root.getChildren().add((Node) mShoots.get(mShoots.size() - 1));
        }

        if (waveType == 4 || waveType == 5) {
            int getShootingMonsterIndex = rand(0, monstersCircular.size() - 1);
            mShoots.add(shootMonster(monstersCircular.get(getShootingMonsterIndex).getLayoutX() + 25, monstersCircular.get(getShootingMonsterIndex).getLayoutY() + 25));
            root.getChildren().add((Node) mShoots.get(mShoots.size() - 1));
        }


    }


    private void monstersShootUpdate() {
        if (!mShoots.isEmpty()) {
            for (int i = 0; i < mShoots.size(); i++) {
                mShoots.get(i).setLayoutY(mShoots.get(i).getLayoutY() + 3);
                if (mShoots.get(i).getLayoutY() <= 0) {
                    root.getChildren().remove(mShoots.get(i));
                    mShoots.remove(i);
                }
            }
        }
    }

    private void isMonsterDestroyed() {

        if (waveType == 2) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monsters.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monsters.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monsters.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monsters.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monsters.get(j).getLayoutY() + 50))))) {


                        if (j != bossPosition) {

                            if (j < bossPosition) {
                                bossPosition -= 1;
                            }
                            root.getChildren().remove(monsters.get(j));
                            monsters.removes(j);
                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10;
                            points.setText("Points: " + String.valueOf(numPoints));


                        } else if (j == bossPosition && bosslives != 1) {

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            bosslives -= 1;


                        } else if (j == bossPosition && bosslives == 1) {

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10 * monsters.size();
                            points.setText("Points: " + String.valueOf(numPoints));

                            if (monsters.size() != 1) {
                                for (int k = 0; k < 6; k++) {
                                    root.getChildren().remove(monsters.get(0));
                                    monsters.removes(0);
                                }
                            } else {
                                root.getChildren().remove(monsters.get(j));
                                monsters.removes(j);
                            }


                        }
                    }
                }
            }
        }
        if (waveType == 1) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monsters.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monsters.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monsters.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monsters.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monsters.get(j).getLayoutY() + 50))))) {

                        root.getChildren().remove(monsters.get(j));
                        monsters.removes(j);
                        root.getChildren().remove(pShoots.get(i));
                        pShoots.remove(i);
                        numPoints += 10;
                        points.setText("Points: " + String.valueOf(numPoints));


                    }
                }
            }
        }

        if (waveType == 3) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monstersDouble.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monstersDouble.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monstersDouble.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monstersDouble.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monstersDouble.get(j).getLayoutY() + 50))))) {

                        if (j != bossPosition) {

                            if (j < bossPosition) {
                                bossPosition -= 1;
                            }
                            root.getChildren().remove(monstersDouble.get(j));
                            monstersDouble.removes(j);
                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10;
                            points.setText("Points: " + String.valueOf(numPoints));


                        } else if (j == bossPosition && bosslives != 1){

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            bosslives -= 1;


                        } else if (j == bossPosition && bosslives == 1) {

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10 * monstersDouble.size();
                            points.setText("Points: " + String.valueOf(numPoints));

                            if (monstersDouble.size() != 1) {
                                for (int k = 0; k < 6; k++) {
                                    root.getChildren().remove(monstersDouble.get(0));
                                    monstersDouble.removes(0);
                                }
                            } else {
                                root.getChildren().remove(monstersDouble.get(j));
                                monstersDouble.removes(j);
                            }

                        }
                    }
                }
            }
        }

        if (waveType == 4) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monstersCircular.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monstersCircular.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monstersCircular.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monstersCircular.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monstersCircular.get(j).getLayoutY() + 50))))) {





                        if (j != bossPosition) {

                            if (j < bossPosition) {
                                bossPosition -= 1;
                            }
                            root.getChildren().remove(monstersCircular.get(j));
                            monstersCircular.removes(j);
                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10;
                            points.setText("Points: " + String.valueOf(numPoints));


                        } else if (j == bossPosition && bosslives != 1) {

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            bosslives -= 1;


                        } else if (j == bossPosition && bosslives <= 1) {

                            if(monstersCircular.size() > 1){

                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));
                                bosslives = rand(2,5);

                                monsterWaveC();

                            }else if(monstersCircular.size() <= 1){

                                root.getChildren().remove(monstersCircular.get(0));
                                monstersCircular.removes(0);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10 * monstersCircular.size();
                                points.setText("Points: " + String.valueOf(numPoints));

                            }

                        }
                    }
                }
            }
        }

        if (waveType == 5) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monstersCircular.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monstersCircular.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monstersCircular.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monstersCircular.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monstersCircular.get(j).getLayoutY() + 50))))) {

/*
                        if (j == enemyPosition0 ){


                            if(live1 == 1){
                                System.out.println("Primero");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));
                                live1++;


                            }else{
                                live1--;
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);

                            }

                        }
                        if (j == enemyPosition1){

                            if (live2 == 1){

                                System.out.println("segundo");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));



                            }else{
                                live2--;

                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);


                            }


                        }

                        if (j == enemyPosition2){
                            System.out.println("tercero");

                            if (live3 == 1){

                                System.out.println("segundo");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));



                            }else{
                                live3--;
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);


                            }


                        }
                        if (j == bossPosition){


                            System.out.println("boss");

                            if (bosslives == 1){

                                System.out.println("segundo");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));


                            }else{
                                bosslives--;
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                            }


                        }
                        if (j == enemyPosition4){
                            if (live4 == 1){

                                System.out.println("segundo");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));



                            }else{

                                live4--;
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);


                            }

                        }
                        if (j == enemyPosition5){
                            if (live5 == 1){

                                System.out.println("segundo");

                                root.getChildren().remove(monstersCircular.get(j));
                                monstersCircular.removes(j);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));



                            }else{
                                live5--;
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);


                            }

                        }

 */



                        if (j != bossPosition) {

                            if (j < bossPosition) {
                                bossPosition -= 1;
                            }
                            root.getChildren().remove(monstersCircular.get(j));
                            monstersCircular.removes(j);
                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            numPoints += 10;
                            points.setText("Points: " + String.valueOf(numPoints));


                        } else if (j == bossPosition && bosslives != 1) {

                            root.getChildren().remove(pShoots.get(i));
                            pShoots.remove(i);
                            bosslives -= 1;


                        } else if (j == bossPosition && bosslives <= 1) {

                            if (monstersCircular.size() > 1) {

                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10;
                                points.setText("Points: " + String.valueOf(numPoints));
                                bosslives = rand(2, 5);

                                monsterWaveC();

                            } else if (monstersCircular.size() <= 1) {

                                root.getChildren().remove(monstersCircular.get(0));
                                monstersCircular.removes(0);
                                root.getChildren().remove(pShoots.get(i));
                                pShoots.remove(i);
                                numPoints += 10 * monstersCircular.size();
                                points.setText("Points: " + String.valueOf(numPoints));
                            }
                        }
                    }
                }
            }
        }
        if (waveType == 6) {

            for (int i = 0; i < pShoots.size(); i++) {
                for (int j = 0; j < monstersDouble.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monstersDouble.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monstersDouble.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monstersDouble.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monstersDouble.get(j).getLayoutY() + 50))))) {

                        root.getChildren().remove(monstersDouble.get(j));
                        monstersDouble.removes(j);
                        root.getChildren().remove(pShoots.get(i));
                        pShoots.remove(i);
                        numPoints += 10;
                        points.setText("Points: " + String.valueOf(numPoints));


                    }
                }
            }

        }
    }


    public void monsterWaveC(){

        int newPoss = rand(0,monstersCircular.size());

        if (newPoss == bossPosition){
            monsterWaveC();
        }else{


            double newX = monstersCircular.get(newPoss).getLayoutX();
            double newY = monstersCircular.get(newPoss).getLayoutY();

            monstersCircular.get(bossPosition).setLayoutX(newX);
            monstersCircular.get(bossPosition).setLayoutY(newY);



            root.getChildren().remove(monstersCircular.get(newPoss));
            monstersCircular.removes(newPoss);


            if (newPoss < bossPosition){
                bossPosition-=1;
                System.out.println("Bos poss: " + bossPosition);
            }

        }


    }



    public ImageView player() {
        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Player.png")));
        i.setLayoutX(225);
        i.setLayoutY(630);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }



    public ImageView monster(double x, double y) {

        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Invader.png")));
        i.setLayoutX(x);
        i.setLayoutY(y);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }



    public ImageView monsterBoss(double x, double y) {
        if (waveType == 5){
            bosslives = 4;
            ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Boss.png")));
            i.setLayoutX(x);
            i.setLayoutY(y);
            i.setFitHeight(50);
            i.setFitWidth(50);
            return i;

        }else {
            bosslives = rand(2, 5);
            ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Boss.png")));
            i.setLayoutX(x);
            i.setLayoutY(y);
            i.setFitHeight(50);
            i.setFitWidth(50);
            return i;
        }
    }

    public void playerShoot(double x) {
        pShoots.add(shoot(player.getLayoutX() + 25, player.getLayoutY() + 25));
        root.getChildren().add(pShoots.get(pShoots.size() - 1));
    }

    public void playersShootUpdate() {
        if (!pShoots.isEmpty()) {
            for (int i = 0; i < pShoots.size(); i++) {
                pShoots.get(i).setLayoutY(pShoots.get(i).getLayoutY() - 3);
                if (pShoots.get(i).getLayoutY() <= 0) {
                    root.getChildren().remove(pShoots.get(i));
                    pShoots.remove(i);
                }
            }
        }
    }

    private void isPlayerDestroyed() {
        for (int i = 0; i < mShoots.size(); i++) {
            if (((mShoots.get(i).getLayoutX() > player.getLayoutX())
                    && ((mShoots.get(i).getLayoutX() < player.getLayoutX() + 50))
                    && ((mShoots.get(i).getLayoutY() > player.getLayoutY())
                    && ((mShoots.get(i).getLayoutY() < player.getLayoutY() + 50))))) {
                player.setLayoutX(225);
                numLives -= 1;
                lives.setText("Lives: " + String.valueOf(numLives));
            }
        }
    }

    public Circle shoot(double x, double y) {
        Circle c = new Circle();
        c.setFill(Color.GREENYELLOW);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setRadius(3);
        return c;
    }

    public Circle shootMonster(double x, double y) {
        Circle c = new Circle();
        c.setFill(Color.RED);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setRadius(3);
        return c;
    }


    public void isWin() {

        if (waveType == 1 || waveType == 2) {
            if (monsters.size() <= 0 && numPoints < 200) {

                root.getChildren().removeAll(pShoots);
                pShoots.removeAll(pShoots);
                counter++;
                addMonsters();

            }
            if (numPoints >= 200 && monsters.size() == 0) {
                Text text = new Text();
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setX(80);
                text.setY(300);
                text.setFill(Color.YELLOW);
                text.setStrokeWidth(3);
                text.setStroke(Color.CRIMSON);
                text.setText("WIN & Points: " + numPoints);
                root.getChildren().add(text);
                timer.stop();
            }
        }


        if (waveType == 3) {

            if (monstersDouble.size() <= 0 && numPoints < 200) {

                root.getChildren().removeAll(pShoots);
                pShoots.removeAll(pShoots);
                counter++;
                addMonsters();

            }
            if (numPoints >= 200 && monsters.size() == 0) {
                Text text = new Text();
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setX(80);
                text.setY(300);
                text.setFill(Color.YELLOW);
                text.setStrokeWidth(3);
                text.setStroke(Color.CRIMSON);
                text.setText("WIN & Points: " + numPoints);
                root.getChildren().add(text);
                timer.stop();
            }
        }

        if (waveType == 4 || waveType == 5) {

            if (monstersCircular.size() <= 0 && numPoints < 200) {

                root.getChildren().removeAll(pShoots);
                pShoots.removeAll(pShoots);
                counter++;
                addMonsters();

            }
            if (numPoints >= 200 && monsters.size() == 0) {
                Text text = new Text();
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
                text.setX(80);
                text.setY(300);
                text.setFill(Color.YELLOW);
                text.setStrokeWidth(3);
                text.setStroke(Color.CRIMSON);
                text.setText("WIN & Points: " + numPoints);
                root.getChildren().add(text);
                timer.stop();
            }
        }
    }

    public void isLost() {
        if (numLives <= 0) {
            Text text = new Text();
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
            text.setX(80);
            text.setY(300);
            text.setFill(Color.YELLOW);
            text.setStrokeWidth(3);
            text.setStroke(Color.CRIMSON);
            text.setText("LOST & Points: " + numPoints);
            root.getChildren().add(text);
            timer.stop();

        }
    }
}



