package com.abhishek360.dev;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sun.corba.se.impl.oa.toa.TOA;

import java.util.ArrayList;
import java.util.List;


public class FloorSimulation extends ApplicationAdapter implements GestureDetector.GestureListener
{
	private final BluetoothApi bluetoothApi;

	private SpriteBatch batch,uiBatch;
	private OrthographicCamera camera;
	public static float FRAME_HEIGHT ,FRAME_WIDTH,PANEL_WIDTH,ICON_HEIGHT,ICON_WIDTH,HORI_SPACING,VERT_SPACING,FLOOR_HEIGHT,FLOOR_WIDTH,TILE_WIDTH,TILE_HEIGHT,CENTER_X,CENTER_Y,HOLDER_WIDTH,HOLDER_HEIGHT;
	private Texture img,floor_image,bed_image,big_sofa_image,left_panel,right_panel;
	private Texture bed_double_image,bed_single_image,bed_double_edge_image,bed_single_edge_image;
	private Texture rotateRight,rotateLeft,save,clearAll,sofaIcon,bedIcon,diningIcon,shape_one,shape_two;
	private Sprite spriteBed,spriteCurrShape,spriteCurrBed,spriteDelete,spriteSofa,spriteCurrSofa,spriteFloor;
	private Sprite spriteRotateRight,spriteRotateLeft,spriteSave,spriteClearAll,spriteCurrItem,nameSprite,spriteAltDelete;
	private GestureDetector gestureDetector;
	private Toast.ToastFactory toastFactory;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private Toast toast;
	private Rectangle rectTile;
	private String currItem = "NONE";
	private List<Rectangle> rectTileBounds;
	private int[] heightArray;
	private int bedStartBound=0,sofaStartBound=0,shapeStartBound=0;
	private boolean touchDownSofa=false,touchDownBed=false,touchDownShape,drawBed=false,drawSofa=false,drawBound=false,drawShape=false;
	private Stage stage;
	private Image imageLeftPanel;
	private Texture background,shalution_name;
	private Color myYellow,myRed,myViolet,myCyan,myRoyal,myGreen,myWhite;
	private NavigationDrawer drawer,bedHolder,shapeHolder,sofaHolder;
	private boolean openBin=false;

	public FloorSimulation(BluetoothApi bluetoothApi)
	{

		this.bluetoothApi = bluetoothApi;
	}

	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		uiBatch= new SpriteBatch();
		myCyan= new Color(Color.CYAN);
		myYellow= new Color(Color.YELLOW);
		myRed= new Color(Color.RED);
		myViolet= new Color(Color.VIOLET);
		myRoyal= new Color(Color.ROYAL);
		myGreen= new Color(Color.GREEN);
		myWhite= new Color(0.2f,0.2F,0.2f,0.1f);
		heightArray= new int[30];
		gestureDetector = new GestureDetector(this);
		font= new BitmapFont();
		font.getData().setScale(3f);


		shapeRenderer= new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		rectTileBounds= new ArrayList<Rectangle>();


		floor_image = new Texture("floor.png");
		shalution_name= new Texture("shalution_name.png");


		FRAME_HEIGHT=Gdx.graphics.getHeight();
		FRAME_WIDTH=Gdx.graphics.getWidth();
		PANEL_WIDTH=FRAME_WIDTH/10;
		ICON_HEIGHT=FRAME_HEIGHT/8.5f;
		ICON_WIDTH=FRAME_WIDTH/15;
		HORI_SPACING= FRAME_WIDTH/192;
		VERT_SPACING=FRAME_HEIGHT/54;
		FLOOR_HEIGHT=FRAME_HEIGHT/1.3f;
		FLOOR_WIDTH=floor_image.getWidth()*FLOOR_HEIGHT/floor_image.getHeight();
		//FLOOR_WIDTH=FRAME_WIDTH/1.8f;
		TILE_WIDTH=FLOOR_WIDTH/6;
		TILE_HEIGHT=FLOOR_HEIGHT/5;
		HOLDER_HEIGHT=ICON_HEIGHT*4+VERT_SPACING*6;
		HOLDER_WIDTH=ICON_WIDTH*4+HORI_SPACING*6;
		//toast=toastFactory.create( "Tile Height: "+TILE_HEIGHT+"\nTile Width: "+TILE_WIDTH, Toast.Length.LONG);

		toastFactory = new Toast.ToastFactory.Builder()
				.font(font).margin(20).fontColor(myRed).backgroundColor(myWhite).positionY((int)ICON_HEIGHT/2)
				.build();
		toast=toastFactory.create( "Hello! User", Toast.Length.SHORT);


		camera=new OrthographicCamera();
		camera.setToOrtho(false,FRAME_WIDTH,FRAME_HEIGHT);


		//img = new Texture("badlogic.jpg");
		Texture logo= new Texture("shalution_icon.png");
		Texture menu_icon= new Texture("menue_icon.png");
		bed_image = new Texture("bed_2d.png");
		left_panel= new Texture("left_panel.jpg");
		right_panel= new Texture("right_panel.jpg");
		rotateLeft= new Texture("rotate_left.png");
		rotateRight= new Texture("rotate_right.png");
		save= new Texture("save_icon.png");
		//clearAll= new Texture("clear_all.png");
		background= new Texture("shalution_background.png");


		big_sofa_image= new Texture("big_sofa_image.jpg");

		sofaIcon= new Texture("sofa_icon.png");
		bedIcon= new Texture("bed_icon.png");
		diningIcon= new Texture("dining_icon.png");


		//spriteBed = new Sprite(bed_image);
		//spriteSofa= new Sprite(big_sofa_image);
		nameSprite= new Sprite(shalution_name);
		nameSprite.setSize((shalution_name.getWidth()*ICON_HEIGHT*2f)/(shalution_name.getHeight()*4),ICON_HEIGHT*2f/4);
		nameSprite.setPosition(PANEL_WIDTH,FRAME_HEIGHT-VERT_SPACING-ICON_HEIGHT/2-nameSprite.getHeight()/2);
		spriteFloor= new Sprite(floor_image);
		spriteRotateRight= new Sprite(rotateRight);
		spriteRotateRight.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,FRAME_HEIGHT-ICON_HEIGHT-VERT_SPACING);
		spriteRotateRight.setSize(ICON_WIDTH,ICON_HEIGHT);


		spriteRotateLeft= new Sprite(rotateLeft);
		spriteRotateLeft.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,FRAME_HEIGHT-(ICON_HEIGHT+VERT_SPACING)*2);
		spriteRotateLeft.setSize(ICON_WIDTH,ICON_HEIGHT);

		spriteSave= new Sprite(save);
		spriteSave.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,FRAME_HEIGHT-(ICON_HEIGHT+VERT_SPACING)*3);
		spriteSave.setSize(ICON_WIDTH,ICON_HEIGHT);

		/*spriteClearAll= new Sprite(clearAll);
		spriteClearAll.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,FRAME_HEIGHT-(ICON_HEIGHT+VERT_SPACING)*4);
		spriteClearAll.setSize(ICON_WIDTH,ICON_HEIGHT);*/


		spriteFloor.setSize(FLOOR_WIDTH,FLOOR_HEIGHT);
		spriteFloor.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
		spriteFloor.setPosition((FRAME_WIDTH-FLOOR_WIDTH)/2,(FRAME_HEIGHT-FLOOR_HEIGHT)/2);
		rectTile= new Rectangle(spriteFloor.getX(),spriteFloor.getY(),TILE_WIDTH,TILE_HEIGHT);
		for(int i=0;i<6;i++)
		{
			rectTileBounds.add(new Rectangle(spriteFloor.getBoundingRectangle().getX()+(spriteFloor.getBoundingRectangle().getWidth()/6)*(i),spriteFloor.getBoundingRectangle().getY(),spriteFloor.getBoundingRectangle().getWidth()/6,spriteFloor.getBoundingRectangle().getHeight()/5));
			rectTileBounds.add(new Rectangle(spriteFloor.getX()+(spriteFloor.getWidth()/6)*(i),spriteFloor.getY()+(spriteFloor.getHeight()/5),TILE_WIDTH,TILE_HEIGHT));
			rectTileBounds.add(new Rectangle(spriteFloor.getX()+(spriteFloor.getWidth()/6)*(i),spriteFloor.getY()+(spriteFloor.getHeight()/5)*2,TILE_WIDTH,TILE_HEIGHT));
			rectTileBounds.add(new Rectangle(spriteFloor.getX()+(spriteFloor.getWidth()/6)*(i),spriteFloor.getY()+(spriteFloor.getHeight()/5)*3,TILE_WIDTH,TILE_HEIGHT));
			rectTileBounds.add(new Rectangle(spriteFloor.getX()+(spriteFloor.getWidth()/6)*(i),spriteFloor.getY()+(spriteFloor.getHeight()/5)*4,TILE_WIDTH,TILE_HEIGHT));

		}



		spriteDelete= new Sprite(new Texture("delete.png"));
		spriteDelete.setSize(ICON_WIDTH,ICON_HEIGHT);
		spriteDelete.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,VERT_SPACING);

		spriteAltDelete= new Sprite(new Texture("open_delete.png"));
		spriteAltDelete.setSize(ICON_WIDTH,ICON_HEIGHT);
		spriteAltDelete.setPosition(FRAME_WIDTH-ICON_WIDTH-HORI_SPACING,VERT_SPACING);

		/*spriteBed.setSize(ICON_WIDTH,spriteBed.getHeight()*ICON_WIDTH/spriteBed.getWidth());
		spriteBed.setPosition(HORI_SPACING, FRAME_HEIGHT-spriteBed.getHeight()-VERT_SPACING);

		spriteSofa.setSize(spriteSofa.getWidth()/2,spriteSofa.getHeight()/2);
		spriteSofa.setPosition(HORI_SPACING, FRAME_HEIGHT-spriteSofa.getHeight()-spriteBed.getHeight()-VERT_SPACING*2);*/


		//spriteBed.setCenter(0.5f,0.5f);
		stage = new Stage(new StretchViewport(FRAME_WIDTH, FRAME_HEIGHT));


		// May be you want to make some Actions with NavigationDrawer state
		Image sofa_image_drawer=new Image(sofaIcon);

		Image bed_image_drawer=new Image(bedIcon);

		Image dining_image_drawer= new Image(diningIcon);
		Image cube_image_drawer= new Image(new Texture("cube_icon.png"));
		Image shape_one_image_drawer= new Image(new Texture("shape_one.png"));
		//Image shape_two_image_drawer= new Image(new Texture("shape_two.png"));



		Image holderBack=new Image(new Texture("holder_background.jpg"));
		Image bed_single = new Image(new Texture("single_bed.png"));
		Image bed_single_edge = new Image(new Texture("single_bed_edge.png"));
		Image bed_double = new Image(new Texture("double_bed.png"));
		Image bed_double_edge = new Image(new Texture("double_bed_edge.png"));

		Image sofa_one = new Image(new Texture("sofa_one.png"));
		Image sofa_two = new Image(new Texture("sofa_two.png"));
		Image sofa_three = new Image(new Texture("sofa_three.png"));
		Image sofa_lshape = new Image(new Texture("sofa_lshape.png"));

		sofaHolder= new NavigationDrawer(HOLDER_WIDTH,HOLDER_HEIGHT,PANEL_WIDTH,FRAME_HEIGHT-HOLDER_HEIGHT-ICON_HEIGHT*1-VERT_SPACING*6);
		sofaHolder.add(sofa_one).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		sofaHolder.add(sofa_two).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).row();
		sofaHolder.add(sofa_three).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		sofaHolder.add(sofa_lshape).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		sofaHolder.setEnableDrag(false);
		sofaHolder.setBackground(holderBack.getDrawable());
		sofaHolder.setTouchable(Touchable.enabled);
		sofa_one.setName("SOFA_ONE");
		sofa_two.setName("SOFA_TWO");
		sofa_three.setName("SOFA_THREE");
		sofa_lshape.setName("SOFA_LSHAPE");

		stage.addActor(sofaHolder);


		shapeHolder= new NavigationDrawer(HOLDER_WIDTH,HOLDER_HEIGHT,PANEL_WIDTH,-0);
		shapeHolder.add(shape_one_image_drawer).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		shapeHolder.add().size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).row();
		shapeHolder.add().size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		shapeHolder.add().size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);

		shapeHolder.setEnableDrag(false);
		shapeHolder.setBackground(holderBack.getDrawable());
		shapeHolder.setTouchable(Touchable.enabled);
		shape_one_image_drawer.setName("SHAPE_ONE");
		//shape_two_image_drawer.setName("SHAPE_TWO");
		//bed_double.setName("BED_DOUBLE");
		//bed_double_edge.setName("BED_DOUBLE_EDGE");

		stage.addActor(shapeHolder);


		bedHolder= new NavigationDrawer(HOLDER_WIDTH,HOLDER_HEIGHT,PANEL_WIDTH,FRAME_HEIGHT-HOLDER_HEIGHT-ICON_HEIGHT*2-VERT_SPACING*6);
		bedHolder.add(bed_single).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		bedHolder.add(bed_single_edge).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).row();
		bedHolder.add(bed_double).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);
		bedHolder.add(bed_double_edge).size(ICON_WIDTH*2, ICON_HEIGHT*2).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING);


		bedHolder.setEnableDrag(false);

		bedHolder.setBackground(holderBack.getDrawable());
		bedHolder.setTouchable(Touchable.enabled);
		bed_single.setName("BED_SINGLE");
		bed_single_edge.setName("BED_SINGLE_EDGE");
		bed_double.setName("BED_DOUBLE");
		bed_double_edge.setName("BED_DOUBLE_EDGE");

		stage.addActor(bedHolder);


		drawer = new NavigationDrawer(PANEL_WIDTH, FRAME_HEIGHT,0,0);
		//drawer.add(new Image(logo)).size(ICON_WIDTH, logo.getHeight()*ICON_WIDTH/logo.getWidth()).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).expandX().row();
		drawer.add().size(ICON_WIDTH,ICON_HEIGHT*1.25f+VERT_SPACING*2).row(); // empty
		drawer.add(sofa_image_drawer).size(ICON_WIDTH, ICON_HEIGHT).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).expandX().row();
		drawer.add(bed_image_drawer).size(ICON_WIDTH, ICON_HEIGHT).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).expandX().row();
		drawer.add(dining_image_drawer).size(ICON_WIDTH, ICON_HEIGHT).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).expandX().row();
		drawer.add(cube_image_drawer).size(ICON_WIDTH, ICON_HEIGHT).pad(VERT_SPACING, HORI_SPACING, VERT_SPACING, HORI_SPACING).expandX().row();

		imageLeftPanel=  new Image(left_panel);

		Image button_menu= new Image(logo);

		drawer.setBackground(imageLeftPanel.getDrawable());

		drawer.top().left();
		//drawer.setSpeed(25f);
		//drawer.setWidthStartDrag(50f);
		//drawer.setWidthBackDrag(0f);
		drawer.setTouchable(Touchable.enabled);


		//image_background.setFillParent(true);
		//stage.addActor(image_background);
		//drawer.setFadeBackground(image_background, 0.5f);
		//stage.addActor();
		stage.addActor(drawer);


		button_menu.setSize(ICON_WIDTH,ICON_HEIGHT);
		button_menu.setPosition(HORI_SPACING*3,FRAME_HEIGHT-ICON_HEIGHT-VERT_SPACING);
		button_menu.setOrigin(Align.center);
		stage.addActor(button_menu);
		drawer.setRotateMenuButton(button_menu, 360f);

		drawer.showManually(true);

		sofa_image_drawer.setName("SOFA");
		bed_image_drawer.setName("BED");
		button_menu.setName("BUTTON_MENU");
		dining_image_drawer.setName("DINING");
		cube_image_drawer.setName("SHAPES");
		imageLeftPanel.setName("IMAGE_BACKGROUND");


		ClickListener listener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				boolean closed = drawer.isCompletelyClosed();
				boolean bedHolderClosed= bedHolder.isCompletelyClosed();
				boolean shapeHolderClosed= shapeHolder.isCompletelyClosed();
				boolean sofaHolderClosed= sofaHolder.isCompletelyClosed();

				Actor actor = event.getTarget();
				//toast=toastFactory.create( "Actor"+actor,Toast.Length.SHORT);


				if (actor.getName().equals("SOFA"))
				{

					if(!shapeHolderClosed) shapeHolder.showManually(shapeHolderClosed);
					if(!bedHolderClosed) bedHolder.showManually(bedHolderClosed);


					sofaHolder.showManually(sofaHolderClosed);
					drawer.setEnableDrag(!sofaHolderClosed);



				}
				else if (actor.getName().equals("BED"))
				{

					if(!shapeHolderClosed) shapeHolder.showManually(shapeHolderClosed);
					if(!sofaHolderClosed) sofaHolder.showManually(sofaHolderClosed);


					bedHolder.showManually(bedHolderClosed);
					drawer.setEnableDrag(!bedHolderClosed);





				}
				else if (actor.getName().equals("DINING"))
				{


					toast=toastFactory.create( "Dining Table Currently Disabled.",Toast.Length.SHORT);





				}
				else if (actor.getName().equals("SHAPES"))
				{
					if (!bedHolderClosed)
					{
						//toast=toastFactory.create( "Closed: "+bedHolderClosed,Toast.Length.SHORT);

						bedHolder.showManually(bedHolderClosed);
					}
					if(!sofaHolderClosed) sofaHolder.showManually(sofaHolderClosed);




					shapeHolder.showManually(shapeHolderClosed);
					drawer.setEnableDrag(!shapeHolderClosed);





				}
				else if (actor.getName().equals("BED_SINGLE")||actor.getName().equals("BED_SINGLE_EDGE"))
				{
					//touchDownBed = true;


					//spriteBed.setPosition(0,0);


					bedHolder.showManually(bedHolderClosed);
					drawer.setEnableDrag(!bedHolderClosed);


					if (!drawBed)
					{
						//toast=toastFactory.create("bed button clicked.",Toast.Length.SHORT);

						spriteCurrBed = new Sprite(bed_image);
						spriteCurrBed.setSize(TILE_WIDTH * 2 - 10, TILE_HEIGHT * 4 - 10);
						//spriteCurrBed.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
						spriteCurrBed.setPosition(rectTileBounds.get(bedStartBound).getX() + 5, rectTileBounds.get(bedStartBound).getY() + 5);

						//toast= toastFactory.create("OriginX: "+FRAME_WIDTH/2+"\nOriginY: "+FRAME_HEIGHT/2,Toast.Length.LONG);
						//spriteCurrBed.setPosition(x - spriteCurrBed.getWidth() / 2,  y - spriteCurrBed.getHeight() / 2);
						spriteCurrBed.setAlpha(0.6f);
					}
					//toast=toastFactory.create( "Bed Currently Disabled.",Toast.Length.SHORT);

					drawBed=true;



				}
				else if (actor.getName().equals("BED_DOUBLE")||actor.getName().equals("BED_DOUBLE_EDGE"))
				{
					//touchDownBed = true;


					//spriteBed.setPosition(0,0);
					bedHolder.showManually(bedHolderClosed);
					drawer.setEnableDrag(!bedHolderClosed);


					if (!drawBed)
					{
						//toast=toastFactory.create("bed button clicked.",Toast.Length.SHORT);

						spriteCurrBed = new Sprite(bed_image);
						spriteCurrBed.setSize(TILE_WIDTH * 3 - 10, TILE_HEIGHT * 4 - 10);
						//spriteCurrBed.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
						spriteCurrBed.setPosition(rectTileBounds.get(bedStartBound).getX() + 5, rectTileBounds.get(bedStartBound).getY() + 5);

						//toast= toastFactory.create("OriginX: "+FRAME_WIDTH/2+"\nOriginY: "+FRAME_HEIGHT/2,Toast.Length.LONG);
						//spriteCurrBed.setPosition(x - spriteCurrBed.getWidth() / 2,  y - spriteCurrBed.getHeight() / 2);
						spriteCurrBed.setAlpha(0.6f);
					}
					//toast=toastFactory.create( "Bed Currently Disabled.",Toast.Length.SHORT);

					drawBed=true;



				}
				else if (actor.getName().equals("SHAPE_ONE")||actor.getName().equals("SHAPE_TWO"))
				{
					//touchDownBed = true;


					//spriteBed.setPosition(0,0);
					shapeHolder.showManually(shapeHolderClosed);
					drawer.setEnableDrag(!shapeHolderClosed);


					if (!drawBed)
					{
						//toast=toastFactory.create("Shape Button Clicked.",Toast.Length.SHORT);

						spriteCurrShape = new Sprite(new Texture("shape_one.png"));
						spriteCurrShape.setSize(TILE_WIDTH - 8, TILE_HEIGHT - 8);
						//spriteCurrBed.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
						spriteCurrShape.setPosition(rectTileBounds.get(bedStartBound).getX() + 5, rectTileBounds.get(bedStartBound).getY() + 5);

						//toast= toastFactory.create("OriginX: "+FRAME_WIDTH/2+"\nOriginY: "+FRAME_HEIGHT/2,Toast.Length.LONG);
						//spriteCurrBed.setPosition(x - spriteCurrBed.getWidth() / 2,  y - spriteCurrBed.getHeight() / 2);
						spriteCurrShape.setAlpha(0.6f);
					}

					drawShape=true;



				}
				if (actor.getName().equals("SOFA_ONE")||actor.getName().equals("SOFA_THREE")||actor.getName().equals("SOFA_TWO")||actor.getName().equals("SOFA_LSHAPE"))
				{




					sofaHolder.showManually(sofaHolderClosed);
					drawer.setEnableDrag(!sofaHolderClosed);

					if (!drawSofa)
					{
						//toast=toastFactory.create( "Sofa Currently Disabled.",Toast.Length.SHORT);

						spriteCurrSofa = new Sprite(big_sofa_image);
						spriteCurrSofa.setSize(TILE_WIDTH * 2 - 10, TILE_HEIGHT * 4 - 10);
						//spriteCurrSofa.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
						spriteCurrSofa.setPosition(rectTileBounds.get(sofaStartBound).getX() + 5, rectTileBounds.get(sofaStartBound).getY() + 5);

						//spriteCurrSofa.setPosition(x - spriteCurrSofa.getWidth() / 2,  y - spriteCurrSofa.getHeight() / 2);
						spriteCurrSofa.setAlpha(0.6f);
					}
					drawSofa=true;


				}
				else if (actor.getName().equals("BUTTON_MENU") || actor.getName().equals("IMAGE_BACKGROUND"))
				{

					imageLeftPanel.setTouchable(closed ? Touchable.enabled : Touchable.disabled);
					drawer.showManually(closed);

				}

			}
		};
		NavDrawerUtils.addListeners(listener, bed_image_drawer, sofa_image_drawer, button_menu, imageLeftPanel,
											bed_double,bed_single,bed_double_edge,bed_single_edge,cube_image_drawer,
													shape_one_image_drawer,dining_image_drawer,sofa_lshape,sofa_one,sofa_two,sofa_three);





		gestureDetector.setLongPressSeconds(0.7f);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);

		inputMultiplexer.addProcessor(gestureDetector);
		Gdx.input.setInputProcessor(inputMultiplexer);

		//Gdx.input.setInputProcessor(stage);

		//Gdx.input.setInputProcessor(gestureDetector);
		//Gdx.input.setInputProcessor(stage);





	}

	@Override
	public void render ()
	{

		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		uiBatch.begin();
		//uiBatch.draw(background,0,0,FRAME_WIDTH,FRAME_HEIGHT);
		uiBatch.end();

		batch.begin();
		spriteFloor.draw(batch);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(spriteFloor.getX()-2,spriteFloor.getY()-2,FLOOR_WIDTH+4,FLOOR_HEIGHT+4,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-3,spriteFloor.getY()-3,FLOOR_WIDTH+6,FLOOR_HEIGHT+6,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-4,spriteFloor.getY()-4,FLOOR_WIDTH+8,FLOOR_HEIGHT+8,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-5,spriteFloor.getY()-5,FLOOR_WIDTH+10,FLOOR_HEIGHT+10,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-6,spriteFloor.getY()-6,FLOOR_WIDTH+12,FLOOR_HEIGHT+12,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-7,spriteFloor.getY()-7,FLOOR_WIDTH+14,FLOOR_HEIGHT+14,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-8,spriteFloor.getY()-8,FLOOR_WIDTH+16,FLOOR_HEIGHT+16,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-9,spriteFloor.getY()-9,FLOOR_WIDTH+18,FLOOR_HEIGHT+18,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-10,spriteFloor.getY()-10,FLOOR_WIDTH+20,FLOOR_HEIGHT+20,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-11,spriteFloor.getY()-11,FLOOR_WIDTH+22,FLOOR_HEIGHT+22,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-12,spriteFloor.getY()-12,FLOOR_WIDTH+24,FLOOR_HEIGHT+24,myCyan,myRoyal,myRoyal,myRoyal);
		shapeRenderer.rect(spriteFloor.getX()-13,spriteFloor.getY()-13,FLOOR_WIDTH+26,FLOOR_HEIGHT+26,myCyan,myRoyal,myRoyal,myRoyal);

		for(int i=0;i<30;i++)
		{


			if(drawBed)
			{
				if (spriteCurrBed.getBoundingRectangle().overlaps(rectTileBounds.get(i)))
				{



					heightArray[i]=450;

					shapeRenderer.rect(rectTileBounds.get(i).getX()+1,rectTileBounds.get(i).getY()+1,rectTileBounds.get(i).getWidth()-2,rectTileBounds.get(i).getHeight()-2,myYellow,myYellow,myYellow,myYellow);
					shapeRenderer.rect(rectTileBounds.get(i).getX()+2,rectTileBounds.get(i).getY()+2,rectTileBounds.get(i).getWidth()-4,rectTileBounds.get(i).getHeight()-4,myYellow,myYellow,myYellow,myYellow);



				}
				else heightArray[i]=0;
				if(rectTileBounds.get(i).contains(spriteCurrBed.getX(),spriteCurrBed.getY()))
				{
					bedStartBound=i;
				}

			}
			else heightArray[i]=0;

			if(drawShape)
			{
				if (spriteCurrShape.getBoundingRectangle().overlaps(rectTileBounds.get(i)))
				{



					heightArray[i]=550;

					shapeRenderer.rect(rectTileBounds.get(i).getX()+1,rectTileBounds.get(i).getY()+1,rectTileBounds.get(i).getWidth()-2,rectTileBounds.get(i).getHeight()-2,myYellow,myYellow,myYellow,myYellow);
					shapeRenderer.rect(rectTileBounds.get(i).getX()+2,rectTileBounds.get(i).getY()+2,rectTileBounds.get(i).getWidth()-4,rectTileBounds.get(i).getHeight()-4,myYellow,myYellow,myYellow,myYellow);



				}
				else
				{
					if(heightArray[i]==550)
					heightArray[i]=0;
				}
				/*if(rectTileBounds.get(i).contains(spriteCurrShape.getX(),spriteCurrShape.getY()))
				{
					bedStartBound=i;
				}*/


			}


			if (drawSofa)
			{
				if (spriteCurrSofa.getBoundingRectangle().overlaps(rectTileBounds.get(i)))
				{



					heightArray[i]=350;

					shapeRenderer.rect(rectTileBounds.get(i).getX()+1,rectTileBounds.get(i).getY()+1,rectTileBounds.get(i).getWidth()-2,rectTileBounds.get(i).getHeight()-2,myYellow,myYellow,myYellow,myYellow);
					shapeRenderer.rect(rectTileBounds.get(i).getX()+2,rectTileBounds.get(i).getY()+2,rectTileBounds.get(i).getWidth()-4,rectTileBounds.get(i).getHeight()-4,myYellow,myYellow,myYellow,myYellow);



				}

				if(rectTileBounds.get(i).contains(spriteCurrSofa.getX(),spriteCurrSofa.getY()))
				{
					sofaStartBound=i;
				}
			}

			if(drawBound)
			{
				if (spriteCurrItem.getBoundingRectangle().overlaps(rectTileBounds.get(i)))
				{


					/*if(currItem.equals("BED"))
					{
						heightArray[i]=450;
					}
					if(currItem.equals("SOFA"))
					{
						heightArray[i]=350;
					}*/
					shapeRenderer.rect(rectTileBounds.get(i).getX()+1,rectTileBounds.get(i).getY()+1,rectTileBounds.get(i).getWidth()-2,rectTileBounds.get(i).getHeight()-2,myGreen,myGreen,myGreen,myGreen);
					shapeRenderer.rect(rectTileBounds.get(i).getX()+2,rectTileBounds.get(i).getY()+2,rectTileBounds.get(i).getWidth()-4,rectTileBounds.get(i).getHeight()-4,myGreen,myGreen,myGreen,myGreen);


				}


			}

			//shapeRenderer.rect(rectTileBounds.get(i).getX()-3,rectTileBounds.get(i).getY()-3,rectTileBounds.get(i).getWidth()+6,rectTileBounds.get(i).getHeight()+6,new Color(Color.GREEN),new Color(Color.GREEN),new Color(Color.GREEN),new Color(Color.GREEN));
		}
		shapeRenderer.end();



		if (drawBed)
		{
			batch.begin();
			spriteCurrBed.draw(batch);
			batch.end();
		}

		if (drawSofa)
		{
			batch.begin();
			spriteCurrSofa.draw(batch);
			batch.end();
		}

		if (drawShape)
		{
			batch.begin();
			spriteCurrShape.draw(batch);
			batch.end();
		}

		uiBatch.begin();
		//uiBatch.draw(left_panel,0,0,PANEL_WIDTH,FRAME_HEIGHT);
		uiBatch.draw(background,FRAME_WIDTH,FRAME_HEIGHT);
		//uiBatch.draw(right_panel,FRAME_WIDTH-PANEL_WIDTH,0,PANEL_WIDTH,FRAME_HEIGHT);
		//spriteBed.draw(uiBatch);
		//spriteSofa.draw(uiBatch);
		if(openBin)
			spriteAltDelete.draw(uiBatch);
			else
				spriteDelete.draw(uiBatch);
		//spriteClearAll.draw(uiBatch);
		spriteSave.draw(uiBatch);
		spriteRotateRight.draw(uiBatch);
		spriteRotateLeft.draw(uiBatch);
		if(drawer.isCompletelyOpened())nameSprite.draw(uiBatch);
		uiBatch.end();





		if(drawBound)
		{
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.rect(spriteCurrItem.getBoundingRectangle().getX()-7,spriteCurrItem.getBoundingRectangle().getY()-7,spriteCurrItem.getBoundingRectangle().getWidth()+14,spriteCurrItem.getBoundingRectangle().getHeight()+10,new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE));
			shapeRenderer.rect(spriteCurrItem.getBoundingRectangle().getX()-8,spriteCurrItem.getBoundingRectangle().getY()-8,spriteCurrItem.getBoundingRectangle().getWidth()+16,spriteCurrItem.getBoundingRectangle().getHeight()+12,new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE));
			shapeRenderer.rect(spriteCurrItem.getBoundingRectangle().getX()-9,spriteCurrItem.getBoundingRectangle().getY()-9,spriteCurrItem.getBoundingRectangle().getWidth()+18,spriteCurrItem.getBoundingRectangle().getHeight()+14,new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE),new Color(Color.BLUE));

			shapeRenderer.end();
		}
		stage.act(Gdx.graphics.getDeltaTime());

		stage.draw();
		toast.render(Gdx.graphics.getDeltaTime());




	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
		uiBatch.dispose();
		shapeRenderer.dispose();
		floor_image.dispose();
		bed_image.dispose();
		left_panel.dispose();
		right_panel.dispose();
		rotateLeft.dispose();
		rotateRight.dispose();
		save.dispose();
		clearAll.dispose();
		background.dispose();


		stage.dispose();


	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		/*if(spriteBed.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{
			touchDownBed = true;
			drawBed=true;

			//spriteBed.setPosition(0,0);
			spriteCurrBed = new Sprite(bed_image);
			spriteCurrBed.setSize(TILE_WIDTH*3-10,TILE_HEIGHT*5-10);
			spriteCurrBed.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);
			//toast= toastFactory.create("OriginX: "+FRAME_WIDTH/2+"\nOriginY: "+FRAME_HEIGHT/2,Toast.Length.LONG);
			spriteCurrBed.setPosition(x - spriteCurrBed.getWidth() / 2, FRAME_HEIGHT - y - spriteCurrBed.getHeight() / 2);
			spriteCurrBed.setAlpha(0.4f);

		}*/

		/*if(spriteSofa.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{
			drawSofa=true;
			touchDownSofa = true;
			//spriteBed.setPosition(0,0);
			spriteCurrSofa = new Sprite(big_sofa_image);
			spriteCurrSofa.setSize(TILE_WIDTH*2-10,TILE_HEIGHT*4-10);
			spriteCurrSofa.setOrigin(FRAME_WIDTH/2,FRAME_HEIGHT/2);

			spriteCurrSofa.setPosition(x - spriteCurrSofa.getWidth() / 2, FRAME_HEIGHT - y - spriteCurrSofa.getHeight() / 2);
			spriteCurrSofa.setAlpha(0.4f);

		}*/

		if(drawSofa)
		{
			if(spriteCurrSofa.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				//drawSofa=true;
				touchDownSofa = true;

				//spriteBed.setPosition(0,0);
				//spriteCurrSofa = new Sprite(big_sofa_image);
				//spriteCurrSofa.setPosition(x - spriteCurrSofa.getWidth() / 2, FRAME_HEIGHT - y - spriteCurrSofa.getHeight() / 2);
				drawBound=true;
				spriteCurrItem=spriteCurrSofa;
				currItem="SOFA";

			}

		}

		if(drawShape)
		{
			if(spriteCurrShape.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				//drawSofa=true;
				touchDownShape = true;

				//spriteBed.setPosition(0,0);
				//spriteCurrSofa = new Sprite(big_sofa_image);
				//spriteCurrSofa.setPosition(x - spriteCurrSofa.getWidth() / 2, FRAME_HEIGHT - y - spriteCurrSofa.getHeight() / 2);
				drawBound=true;
				spriteCurrItem=spriteCurrShape;
				currItem="SHAPE";

			}

		}



		if(drawBed)
		{
			if(spriteCurrBed.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				//drawSofa=true;
				touchDownBed = true;

				//spriteBed.setPosition(0,0);
				//spriteCurrSofa = new Sprite(big_sofa_image);
				//spriteCurrBed.setPosition(x - spriteCurrBed.getWidth() / 2, FRAME_HEIGHT - y - spriteCurrBed.getHeight() / 2);
				drawBound=true;
				spriteCurrItem=spriteCurrBed;
				currItem="BED";

			}

		}





		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		//spriteBed.setPosition(x-spriteBed.getWidth()/2,Gdx.graphics.getHeight()-y-spriteBed.getHeight()/2);

		//toast= toastFactory.create("Tap Count: "+count,Toast.Length.LONG);

		if(count==2)
		{
			camera.zoom=1;
			camera.update();
		}

		if(drawBed)
		{
			if(spriteCurrBed.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				drawBound=true;
				touchDownBed=false;
				spriteCurrItem=spriteCurrBed;
				//spriteCurrItem.setAlpha(0.4f);

			}
		}

		if(drawShape)
		{
			if(spriteCurrShape.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				drawBound=true;
				touchDownShape=false;
				spriteCurrItem=spriteCurrShape;
				//spriteCurrItem.setAlpha(0.4f);

			}
		}

		if(drawSofa)
		{
			if(spriteCurrSofa.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
			{
				drawBound=true;
				touchDownSofa=false;
				spriteCurrItem=spriteCurrSofa;
				//spriteCurrItem.setAlpha(0.4f);

			}
		}


		if(drawBound&&spriteRotateRight.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{

			spriteCurrItem.rotate90(true);
			spriteCurrItem.setSize(spriteCurrItem.getHeight(),spriteCurrItem.getWidth());

		}
		else if(drawBound&&spriteRotateLeft.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{

			spriteCurrItem.rotate90(false);
			spriteCurrItem.setSize(spriteCurrItem.getHeight(),spriteCurrItem.getWidth());


		}
		else if(spriteSave.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{

			//System.out.print("Array= ");
			boolean flag=true;

			if(drawBed&&drawSofa)
			{
				if(spriteCurrBed.getBoundingRectangle().overlaps(spriteCurrSofa.getBoundingRectangle()))
				{
					flag=false;
					toast= toastFactory.create("Warning!\nOverlapping Objects Detected.",Toast.Length.LONG);


				}
			}

			if(drawBed&&drawShape)
			{
				if(spriteCurrBed.getBoundingRectangle().overlaps(spriteCurrShape.getBoundingRectangle()))
				{
					flag=false;
					toast= toastFactory.create("Warning!\nOverlapping Objects Detected.",Toast.Length.LONG);


				}
			}
			if(drawSofa&&drawShape)
			{
				if(spriteCurrSofa.getBoundingRectangle().overlaps(spriteCurrShape.getBoundingRectangle()))
				{
					flag=false;
					toast= toastFactory.create("Warning!\nOverlapping Objects Detected.",Toast.Length.LONG);


				}
			}

			if(drawBed)
			{
				if(!spriteFloor.getBoundingRectangle().contains(spriteCurrBed.getBoundingRectangle()))
				{
					flag=false;

					toast= toastFactory.create("Warning!\nBed Outside Floor Region.",Toast.Length.LONG);

				}
			}

			if (drawSofa)
			{
				if(!spriteFloor.getBoundingRectangle().contains(spriteCurrSofa.getBoundingRectangle()))
				{
					flag=false;

					toast= toastFactory.create("Warning!\nSofa Outside Floor Region.",Toast.Length.LONG);

				}
			}

			if (!drawSofa&&!drawBed&&!drawShape)
			{
				flag=false;

				toast= toastFactory.create("Warning!\nNo Objects Detected.",Toast.Length.LONG);


			}

			if (flag)
			{
				String array="";
				for (int i=0;i<30;i++)
				{
					array= array+" "+heightArray[i];
				}
				toast= toastFactory.create(array,Toast.Length.LONG);
				if(bluetoothApi.isBluetoothAvailable())
				{
					bluetoothApi.turnBluetoothOn(array);

				}
				else
				{
					toast=toastFactory.create("Bluetooth Device Not Available!",Toast.Length.LONG);
				}


			}





		}
		else if(spriteDelete.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{

			toast=toastFactory.create("Long Press to Clear Floor.\nOR\nDrag Item to Remove.",Toast.Length.LONG);

		}


		return true;
	}

	@Override
	public boolean longPress(float x, float y)
	{

		if(spriteDelete.getBoundingRectangle().contains(x,FRAME_HEIGHT-y))
		{

			drawSofa=false;
			drawBed=false;
			drawBound=false;
			drawShape=false;
			touchDownSofa=false;
			touchDownBed=false;
			heightArray=new int[30];

		}
		return true;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{

		if(touchDownBed)
		{
			if (!openBin)
			{
				if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrBed.getBoundingRectangle())) {
					openBin = true;
				}

			}
			else
			{
				if (!spriteAltDelete.getBoundingRectangle().overlaps(spriteCurrBed.getBoundingRectangle())) {
					openBin = false;
				}
			}

            spriteCurrBed.setPosition(x-spriteCurrBed.getWidth()/2,FRAME_HEIGHT-y-spriteCurrBed.getHeight()/2);

		}

		if(touchDownShape)
		{
			if (!openBin)
			{
				if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrShape.getBoundingRectangle())) {
					openBin = true;
				}

			}
			else
			{
				if (!spriteAltDelete.getBoundingRectangle().overlaps(spriteCurrShape.getBoundingRectangle())) {
					openBin = false;
				}
			}

			spriteCurrShape.setPosition(x-spriteCurrShape.getWidth()/2,FRAME_HEIGHT-y-spriteCurrShape.getHeight()/2);

		}

		else if(touchDownSofa)
		{
			if (!openBin)
			{
				if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrSofa.getBoundingRectangle())) {
					openBin = true;
				}
			}
			else
			{
				if (!spriteAltDelete.getBoundingRectangle().overlaps(spriteCurrSofa.getBoundingRectangle())) {
					openBin = false;
				}
			}
			spriteCurrSofa.setPosition(x-spriteCurrSofa.getWidth()/2,FRAME_HEIGHT-y-spriteCurrSofa.getHeight()/2);

		}




		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button)
	{
		if(touchDownBed)
		{
			if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrBed.getBoundingRectangle()))
			{
				drawBed=false;
				drawBound=false;
				openBin=false;
				bedStartBound=0;


			}

			//spriteBed.setPosition(Gdx.input.getX()-spriteBed.getWidth()/2,Gdx.graphics.getHeight()-Gdx.input.getY()-spriteBed.getHeight()/2);
			//spriteBed.setPosition();

		}

		if(touchDownShape)
		{
			if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrShape.getBoundingRectangle()))
			{
				drawShape=false;
				drawBound=false;
				openBin=false;
				shapeStartBound=0;


			}

			//spriteBed.setPosition(Gdx.input.getX()-spriteBed.getWidth()/2,Gdx.graphics.getHeight()-Gdx.input.getY()-spriteBed.getHeight()/2);
			//spriteBed.setPosition();

		}

		if(touchDownSofa)
		{
			if (spriteDelete.getBoundingRectangle().overlaps(spriteCurrSofa.getBoundingRectangle()))
			{
				drawSofa=false;
				drawBound=false;
				openBin=false;
				sofaStartBound=0;

			}

			//spriteBed.setPosition(Gdx.input.getX()-spriteBed.getWidth()/2,Gdx.graphics.getHeight()-Gdx.input.getY()-spriteBed.getHeight()/2);
			//spriteBed.setPosition();

		}
		touchDownSofa=false;
		touchDownBed=false;
        touchDownShape=false;


        if(drawBed)
		{
			spriteCurrBed.setPosition(rectTileBounds.get(bedStartBound).getX()+5,rectTileBounds.get(bedStartBound).getY()+5);

		}
		if(drawSofa)
		{
			spriteCurrSofa.setPosition(rectTileBounds.get(sofaStartBound).getX()+5,rectTileBounds.get(sofaStartBound).getY()+5);

		}

		if(drawShape)
		{
			spriteCurrShape.setPosition(rectTileBounds.get(shapeStartBound).getX()+5,rectTileBounds.get(shapeStartBound).getY()+5);

		}




		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		//spriteBed.setCenter(spriteBed.getX()+spriteBed.getWidth()/2,spriteBed.getY()+spriteBed.getHeight());

		//spriteBed.setSize(distance,distance);
		/*float scaleRatio = (distance)/initialDistance;
		if(scaleRatio!=1)
		{
			spriteFloor.setScale(scaleRatio);


			if(drawBed)
			{

				spriteCurrBed.setScale((distance)/initialDistance);

			}

			if(drawSofa)
			{
				spriteCurrSofa.setScale((distance)/initialDistance);

			}
		}*/


		camera.zoom=(initialDistance)/distance;
		camera.update();
		//toast= toastFactory.create( "Camera Zoom:"+camera.zoom, Toast.Length.SHORT);


		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		float deltaX = pointer2.x-pointer1.x;
		float deltaY = pointer2.y-pointer1.x;

		float angle=(float)Math.atan2((double)deltaX,(double)deltaY)*MathUtils.radiansToDegrees;
		angle +=90f;
		angle=angle*2;
		if (angle<0)
		{
			angle= 360f-(-angle);
		}

		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);
		camera.rotate(-angle,0,0,1);
		camera.update();
		//spriteBed.setCenter((spriteBed.getX()+spriteBed.getWidth()/2)/Gdx.graphics.getWidth(),(spriteBed.getY()+spriteBed.getHeight())/Gdx.graphics.getHeight());
		/*spriteFloor.setRotation(angle);
		if(drawBed)
		{
			spriteCurrBed.setCenter(FRAME_WIDTH/2,FRAME_HEIGHT/2);
			spriteCurrBed.setRotation(-angle);

		}

		if(drawSofa)
		{
			spriteCurrSofa.setRotation(-angle);

		}*/

		//spriteBed.setPosition(0,0);
		return true;
	}

	@Override
	public void pinchStop()
	{

		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);
		camera.update();


	}
}
