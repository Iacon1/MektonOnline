// By Iacon1
// Created 09/11/2021
// A sprite that can move and animate

package GameEngine.EntityTypes;

import GameEngine.Animation;
import GameEngine.Point2D;
import GameEngine.ScreenCanvas;
import GameEngine.Managers.GraphicsManager;
import Utils.SimpleTimer;

public abstract class SpriteEntity extends GameEntity
{
	// Basic sprite variables
	
	protected String texturePath_; // Image path

	protected Point2D pos_; // Position on screen
	
	protected Point2D texturePos_; // Offset on texture sheet
	protected Point2D textureSize_; // Width, height on texture sheet

	// Kinetic variables
	
	protected Point2D targetPos_; // Target position, -1 if none
	protected int speed_ = 0; // Speed, 0 if none
	
	// Animation variables
	
	protected Animation animation_; // Current animation
	protected int frame_ = -1; // Current frame, -1 if not playing
	protected SimpleTimer animTimer_;
	
	// Constructor
	
	public SpriteEntity()
	{
		super();
		
		pos_ = new Point2D(0, 0);
		texturePos_ = new Point2D(0, 0);
		textureSize_ = new Point2D(0, 0);
		targetPos_ = new Point2D(-1, -1);
		animTimer_ = new SimpleTimer();
	}
	
	// Basic sprite functions
	
	/** Sets the sprite.
	 *  If any value is null then that value will not be update.
	 *  Good for animations or changing specific offsets.
	 *  
	 *  @param texturePath Texture sheet to load
	 *  
	 *  @param textureX    X coordinate of top-left corner of texture.
	 *  @param textureY    Y coordinate of top-left corner of texture.
	 *  @param width       Width of texture.
	 *  @param height      Height of texture.
	 */
	public void setSprite(String texturePath, Integer textureX, Integer textureY, Integer width, Integer height) // If any input is null then don't change
	{
		if (texturePath != null) texturePath_ = texturePath;
		if (textureX != null) texturePos_.x_ = textureX;
		if (textureY != null) texturePos_.y_ = textureY;
		if (width != null) textureSize_.x_ = width;
		if (height != null) textureSize_.y_ = height;
	}
	
	public void setPos(Point2D pos)
	{
		pos_ = pos;
	}
	public Point2D getPos()
	{
		return pos_;
	}
	
	@Override
	public void render(ScreenCanvas canvas, Point2D camera) 
	{
		canvas.drawImageScaled(texturePath_, pos_.subtract(camera), texturePos_, textureSize_);
	}
	
	// Kinetic functions
	
	public int getSpeed()
	{
		return speed_;
	}
	
	/** Called when you start moving.*/
	public abstract void onStart(); // When you stop
	/** Called when you stop moving.*/
	public abstract void onStop(); // When you stop
	
	/**
	* Moves by delta (i. e. adds delta to our position).
	* <p>
	*
	* @param  delta how far to move in each direction..
	*/
	public void moveDelta(Point2D delta)
	{
		pos_ = pos_.add(delta);
	}
	/**
	* Moves to pos at a set speed.
	* <p>
	*
	* @param  pos   Where to go to.
	* @param  speed How fast to move.
	*/
	public void moveTargetSpeed(Point2D pos, int speed)
	{
		targetPos_ = pos;
		speed_ = speed;
		if (speed_ != 0) onStart();
	}
	/**
	* Moves by delta at a set speed.
	* <p>
	*
	* @param  delta How far to go.
	* @param  speed How fast to move.
	*/
	public void moveDeltaSpeed(Point2D delta, int speed)
	{
		moveTargetSpeed(pos_.add(delta), speed);
	}

	protected Point2D getSpeedVector(Point2D delta)
	{
		double angle = Math.atan2(delta.y_, delta.x_);
		int sX = (int) (((double) speed_) * Math.cos(angle)); // Directional speeds
		int sY = (int) (((double) speed_) * Math.sin(angle));
		return new Point2D(sX, sY);
	}
	private void updateMove() // Updates movement with speed
	{
		if (targetPos_.equals(new Point2D(-1, -1)) && speed_ == 0) return;
		Point2D delta = new Point2D(targetPos_.x_ - pos_.x_, targetPos_.y_ - pos_.y_);
		
		Point2D speedVector = getSpeedVector(delta);
		
		if (Math.abs(delta.x_) <= Math.abs(speedVector.x_)) pos_.x_ = targetPos_.x_; // We're within speed of target
		else pos_.x_ += speedVector.x_;
		if (Math.abs(delta.y_) <= Math.abs(speedVector.y_)) pos_.y_ = targetPos_.y_; // We're within speed of target
		else pos_.y_ += speedVector.y_;
		
		if (pos_.equals(targetPos_))
		{
			targetPos_ = new Point2D(-1, -1);
			speed_ = 0;
			onStop();
		}
	}

	// Animation functions
	
	private void setFrame(int offset) // In height multiples
	{
		setSprite(null, null, textureSize_.y_ * offset, null, null);
	}
	public void startAnimation(Animation animation)
	{
		animation_ = animation;
		frame_ = 0;
		setFrame(frame_);
		animTimer_.start();
	}
	public void stopAnimation(boolean handle)
	{
		frame_ = -1;
		if (handle) onAnimStop();
	}
	public boolean isAnimPlaying()
	{
		return (frame_ == -1);
	}
	/** Called when a callHandle-type animation stops.*/
	public abstract void onAnimStop();
	private void updateAnim()
	{
		if (frame_ == -1) return;
		if (animTimer_.checkTime(animation_.GetFrameDuration()))
		{
			frame_ += 1;
			if (frame_ >= animation_.frames) switch (animation_.action)
			{
			case stickFrame:
				frame_ = -1;
				break;
			case callHandle:
				frame_ = -1;
				onAnimStop();
				break;
			case loop:
				frame_ = 0;
				break;
			}
		}
	}
	
	// Override functions
	
	@Override
	public void update() {updateAnim(); updateMove();}
}
