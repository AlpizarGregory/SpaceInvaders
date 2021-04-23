package sample;

/**
        *Instituto Tecnologico de Costa Rica

        *Ingeniería en Computadores

        *Lenguaje: Java.
        *Versión: 15.0.1
        *Autor(es): Gregory Gerardo Alpizar Cespedes.
        *Fabian Jesús Castillo Cerdas.
        *Erick Daniel Obando Venegas.
        *Jose Andres Quiros Guzman.

        *Versión: 1.0.1
        *Curso: Algoritmos y Estructura de Datos I.
        *Profesor: Jose Isaac Ramirez Herrera.
        *Grupo: II

        *Fecha de última modificación: 23/04/2021.

*/





//import necessary libraries

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


    //Creation of necessary variables and lists
    AnimationTimer timer;
    Pane root = new Pane();
    MonsterList<ImageView> monsters = new MonsterList<>();
    DoubleMonsterList<ImageView> monstersDouble = new DoubleMonsterList<>();
    CircularMonsterList<ImageView> monstersCircular =  new CircularMonsterList<>();
    DoubleMonsterList<ImageView> monstersDoubleCircular =  new DoubleMonsterList<>();
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
            if (monsters.size() != 0 || monstersDouble.size() != 0 || monstersCircular.size() != 0 || monstersDoubleCircular.size() != 0) {
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


        //Stage characteristics
        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();


    }

    /**
     * For constantly updating the game
     */

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


    /**
     * Creates a random number between a max and a min value
     * @param min
     * @param max
     * @return
     */
    public int rand(int min, int max) {
        return (int) (Math.random() * max + min);
    }

    /**
     * Creates a list with random values which will decide the monster waves types
     */
    public void setRandomList() {
        for (int i = 0; i <= 9; i++) {
            A[i] = (int) (rand(1, 6));

        }
    }


    /**
     * Puts all the monsters in their specific type of list, also displays the level and the types of enemy
     * waves in the screen
     */
    public void addMonsters() {

        waveType = A[counter];

        if (A[counter] == 1){
            wave.setText("Wave type: Basic");

        }
        else if (A[counter] == 2){
            wave.setText("Wave type: A");

        }
        else if (A[counter] == 3){
            wave.setText("Wave type: B");

        }
        else if (A[counter] == 4){
            wave.setText("Wave type: C");

        }
        else if (A[counter] == 5){
            wave.setText("Wave type: D");

        }
        else if (A[counter] == 6){
            wave.setText("Wave type: E");

        }


        if (A[counter +1]  == 1){
            waveNext.setText("Next wave type: Basic");

        }
        else if (A[counter +1]   == 2){
            waveNext.setText("Next wave type: A");

        }
        else if (A[counter +1]  == 3){
            waveNext.setText("Next wave type: B");

        }
        else if (A[counter+1]  == 4){
            waveNext.setText("Next wave type: C");

        }
        else if (A[counter +1]  == 5){
            waveNext.setText("Next wave type: D");

        }
        else if (A[counter +1]   == 6){
            waveNext.setText("Next wave type: E");

        }




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
                    monstersDoubleCircular.add(monsterBoss(w, 80));
                } else {
                    monstersDoubleCircular.add(monster(w, 80));
                }
                root.getChildren().add(monstersDoubleCircular.get(i));
            }


        }

    }


    /**
     *In charge of moving the different types of enemy waves
     */

    public void monstersMove() {

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

            for (int i = 0; i < monstersDoubleCircular.size(); i++) {
                monstersDoubleCircular.get(i).setLayoutY(monstersDoubleCircular.get(i).getLayoutY() + speed);
            }
            dotR.setLayoutY(dotR.getLayoutY() + speed);

            if((monstersDoubleCircular.get(1).getLayoutX() >= 145 && monstersDoubleCircular.get(1).getLayoutX() <= 220) && monstersDoubleCircular.get(1).getLayoutY() <= monstersDoubleCircular.get(bossPosition).getLayoutY()){

                monstersDoubleCircular.get(1).setLayoutX(monstersDoubleCircular.get(1).getLayoutX() + speed);
                monstersDoubleCircular.get(1).setLayoutY(monstersDoubleCircular.get(1).getLayoutY() - speed);
                monstersDoubleCircular.get(0).setLayoutX(monstersDoubleCircular.get(0).getLayoutX() + speed * 2);
                monstersDoubleCircular.get(0).setLayoutY(monstersDoubleCircular.get(0).getLayoutY() - speed * 2);

                monstersDoubleCircular.get(3).setLayoutX(monstersDoubleCircular.get(3).getLayoutX() - speed );
                monstersDoubleCircular.get(3).setLayoutY(monstersDoubleCircular.get(3).getLayoutY() + speed );
                monstersDoubleCircular.get(4).setLayoutX(monstersDoubleCircular.get(4).getLayoutX() - speed * 2);
                monstersDoubleCircular.get(4).setLayoutY(monstersDoubleCircular.get(4).getLayoutY() + speed * 2);

            }
            else if(monstersDoubleCircular.get(1).getLayoutX() >= 220  && monstersDoubleCircular.get(1).getLayoutY() < 230){
                monstersDoubleCircular.get(1).setLayoutX(monstersDoubleCircular.get(1).getLayoutX() + speed);
                monstersDoubleCircular.get(1).setLayoutY(monstersDoubleCircular.get(1).getLayoutY() + speed);
                monstersDoubleCircular.get(0).setLayoutX(monstersDoubleCircular.get(0).getLayoutX() + speed * 2);
                monstersDoubleCircular.get(0).setLayoutY(monstersDoubleCircular.get(0).getLayoutY() + speed * 2);

                monstersDoubleCircular.get(3).setLayoutX(monstersDoubleCircular.get(3).getLayoutX() - speed );
                monstersDoubleCircular.get(3).setLayoutY(monstersDoubleCircular.get(3).getLayoutY() - speed );
                monstersDoubleCircular.get(4).setLayoutX(monstersDoubleCircular.get(4).getLayoutX() - speed * 2);
                monstersDoubleCircular.get(4).setLayoutY(monstersDoubleCircular.get(4).getLayoutY() - speed * 2);

            }
            else if(monstersDoubleCircular.get(1).getLayoutX() >= 220  && monstersDoubleCircular.get(1).getLayoutY() > 230){
                monstersDoubleCircular.get(1).setLayoutX(monstersDoubleCircular.get(1).getLayoutX() - speed);
                monstersDoubleCircular.get(1).setLayoutY(monstersDoubleCircular.get(1).getLayoutY() + speed);
                monstersDoubleCircular.get(0).setLayoutX(monstersDoubleCircular.get(0).getLayoutX() - speed * 2);
                monstersDoubleCircular.get(0).setLayoutY(monstersDoubleCircular.get(0).getLayoutY() + speed * 2);

                monstersDoubleCircular.get(3).setLayoutX(monstersDoubleCircular.get(3).getLayoutX() + speed );
                monstersDoubleCircular.get(3).setLayoutY(monstersDoubleCircular.get(3).getLayoutY() - speed );
                monstersDoubleCircular.get(4).setLayoutX(monstersDoubleCircular.get(4).getLayoutX() + speed * 2);
                monstersDoubleCircular.get(4).setLayoutY(monstersDoubleCircular.get(4).getLayoutY() - speed * 2);

            }
            else if((monstersDoubleCircular.get(1).getLayoutX() >= 144 && monstersDoubleCircular.get(1).getLayoutX() <= 220) && monstersDoubleCircular.get(1).getLayoutY() >= monstersDoubleCircular.get(bossPosition).getLayoutY()){
                monstersDoubleCircular.get(1).setLayoutX(monstersDoubleCircular.get(1).getLayoutX() - speed);
                monstersDoubleCircular.get(1).setLayoutY(monstersDoubleCircular.get(1).getLayoutY() - speed);
                monstersDoubleCircular.get(0).setLayoutX(monstersDoubleCircular.get(0).getLayoutX() - speed * 2);
                monstersDoubleCircular.get(0).setLayoutY(monstersDoubleCircular.get(0).getLayoutY() - speed * 2);

                monstersDoubleCircular.get(3).setLayoutX(monstersDoubleCircular.get(3).getLayoutX() + speed );
                monstersDoubleCircular.get(3).setLayoutY(monstersDoubleCircular.get(3).getLayoutY() + speed );
                monstersDoubleCircular.get(4).setLayoutX(monstersDoubleCircular.get(4).getLayoutX() + speed * 2);
                monstersDoubleCircular.get(4).setLayoutY(monstersDoubleCircular.get(4).getLayoutY() + speed * 2);

            }

        }

    }

    /**
     * Determines the behavior of the B type enemy wave
     */

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


    /**
     * Creates the enemy shoots depending on the actual position of the invaders
     */
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
        if (waveType == 6) {
            int getShootingMonsterIndex = rand(0, monstersDoubleCircular.size() - 1);
            mShoots.add(shootMonster(monstersDoubleCircular.get(getShootingMonsterIndex).getLayoutX() + 25, monstersDoubleCircular.get(getShootingMonsterIndex).getLayoutY() + 25));
            root.getChildren().add((Node) mShoots.get(mShoots.size() - 1));
        }




    }

    /**
     * if a monster shoot leaves the screen it will be removed
     */
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

    /**
     * Collisions for the enemies
     */

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
                for (int j = 0; j < monstersDoubleCircular.size(); j++) {
                    if (((pShoots.get(i).getLayoutX() > monstersDoubleCircular.get(j).getLayoutX())
                            && ((pShoots.get(i).getLayoutX() < monstersDoubleCircular.get(j).getLayoutX() + 50))
                            && ((pShoots.get(i).getLayoutY() > monstersDoubleCircular.get(j).getLayoutY())
                            && ((pShoots.get(i).getLayoutY() < monstersDoubleCircular.get(j).getLayoutY() + 50))))) {

                        root.getChildren().remove(monstersDoubleCircular.get(j));
                        monstersDoubleCircular.removes(j);
                        root.getChildren().remove(pShoots.get(i));
                        pShoots.remove(i);
                        numPoints += 10;
                        points.setText("Points: " + String.valueOf(numPoints));


                    }
                }
            }

        }
    }


    /**
     * Determines the behavior of the C type enemy wave
     */
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


    /**
     * Creates the player
     */
    public ImageView player() {
        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Player.png")));
        i.setLayoutX(225);
        i.setLayoutY(630);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }


    /**
     * Creates the a basic monster
     */
    public ImageView monster(double x, double y) {

        ImageView i = new ImageView(new Image(getClass().getResourceAsStream("Invader.png")));
        i.setLayoutX(x);
        i.setLayoutY(y);
        i.setFitHeight(50);
        i.setFitWidth(50);
        return i;
    }

    /**
     * Creates the a boss monster
     */
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


    /**
     * Displays the player shoots on the screen depending the position of the player
     * @param x
     */
    public void playerShoot(double x) {
        pShoots.add(shoot(player.getLayoutX() + 25, player.getLayoutY() + 25));
        root.getChildren().add(pShoots.get(pShoots.size() - 1));
    }


    /**
     * if a player shoot leaves the screen it will be removed
     */
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

    /**
     * Collisions for the player
     */
    private void isPlayerDestroyed() {
        for (int i = 0; i < mShoots.size(); i++) {
            if (((mShoots.get(i).getLayoutX() > player.getLayoutX())
                    && ((mShoots.get(i).getLayoutX() < player.getLayoutX() + 50))
                    && ((mShoots.get(i).getLayoutY() > player.getLayoutY())
                    && ((mShoots.get(i).getLayoutY() < player.getLayoutY() + 50))))) {
                root.getChildren().remove(mShoots.get(i));
                mShoots.remove(i);
                numLives -= 1;
                lives.setText("Lives: " + String.valueOf(numLives));
            }
        }
    }

    /**
     * Creates the shoots for the player
     * @param x
     * @param y
     * @return
     */
    public Circle shoot(double x, double y) {
        Circle c = new Circle();
        c.setFill(Color.GREENYELLOW);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setRadius(3);
        return c;
    }


    /**
     * Creates the shoots for the monsters
     * @param x
     * @param y
     * @return
     */
    public Circle shootMonster(double x, double y) {
        Circle c = new Circle();
        c.setFill(Color.RED);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setRadius(3);
        return c;
    }

    /**
     * Verifies if theres a win condition
     */
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

    /**
     * Verifies if theres a lose condition
     */
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



