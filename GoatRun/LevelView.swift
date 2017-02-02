//
//  LevelView.swift
//  GoatRun
//
//  Created by Sam Jones on 2016-07-21.
//  Copyright © 2016 Benjamin. All rights reserved.
//
//NOTE: CGRect- the point (x,y) is the point at the bottom right of rectangle
//NOTE: To draw something use the code { didSet{ setNeedsDisplay() } } which will tell the view to redraw it
//Make an indicator for how many moves it can be done in with animations 
//Numbering the logs


import UIKit
import Foundation

extension CollectionType {
    /// Return a copy of `self` with its elements shuffled
    func shuffle() -> [Generator.Element] {
        var list = Array(self)
        list.shuffleInPlace()
        return list
    }
}

extension MutableCollectionType where Index == Int {
    /// Shuffle the elements of `self` in-place.
    mutating func shuffleInPlace() {
        // empty and single-element collections don't shuffle
        if count < 2 { return }
        
        for i in 0..<count - 1 {
            let j = Int(arc4random_uniform(UInt32(count - i))) + i
            guard i != j else { continue }
            swap(&self[i], &self[j])
        }
    }
}

 @IBDesignable 
class LevelView: UIView {
    @IBInspectable
    var scale : CGFloat = CGFloat() { didSet{ setNeedsDisplay() } }
    //amount of Hills in the level
    @IBInspectable
    var amountOfHills : Int = 4 { didSet{ setNeedsDisplay() } }
    @IBInspectable
    var hillWidth : CGFloat = 50.0 { didSet{ setNeedsDisplay() } }
    //every hills height increases by this
    @IBInspectable
    var hillHeightConst : CGFloat = 10.0 { didSet{ setNeedsDisplay() } }
    
    var point : CGPoint = CGPoint(x: 0.0, y: 0.0) {
        didSet{
            setNeedsDisplay()
        }
    }
    @IBInspectable
    var color : UIColor = UIColor.greenColor()
    //last hill that was clicked on
    var selected : Int? = nil
    //the hill that needs to be switched with
    var secondSelected : Int = Int()
    //is this the first time LevelView was Called 
    var firstCall = true
    //Initializer for the array of hill heights
    var hillHeight = [Int]()
    //the array that stores all the hills in the level
    var hills = Array<CGRect>() {didSet{  setNeedsDisplay() } }
    //this var keeps track of whether or not a switch was made
    var done = false
    //this points to a class with helper functions
    var help = HelperClass()
    //this happens when the screen is tapped
    var hillPosInArray = [Int]()
    //array that switches arround the hill heights to check when the player won
    var checkWin = Array<Int>()
    
    var bunnysTestRect = CGRect() { didSet{ setNeedsDisplay()} }
    
    var backDrop = UIImage(named:"gameBackground.png")!
    
    
    
    var won = false
    // dictionary that stores the moves in order to determine if the method is efficient
    var movesStorage = Array<Array<Int>>()
    
    var numOfMoves = 0
    
    var realCounter = 0
    //1 means you just have to go lowest - highest, 2 means lowest - highest - lowest, 3...
    var amountOfDrops = 1
    //this variable is controlled by the levelViewController
    var endGame: Bool = false
    //is false until they exceed the amount of moves they should use
    var usingTooManyMoves = false
    
    var initialSetup = Array<Int>()
    // this is the class responsible for the animation that swaps the blocks
    
    var blockSwapAnimationDidComplete : Bool = true
    
    var globalToSwitch = Int()
    
    var globalSelected = Int()
    
    func swap(recognizer: UITapGestureRecognizer){
        recognizer.numberOfTapsRequired = 1
        switch recognizer.state {
        case .Changed, .Ended:
            if endGame == false{
                point = recognizer.locationInView(self)
            }
            else if endGame == true{
                point = CGPoint(x: -100.0,y: -100.0)
            }
        default:
            break
        }
    }
    
    func checkIfMin(x1: Int, x2:Int) -> Bool
    {
        let array = [x1,x2].sort()
        numOfMoves = numOfMoves + 1
        if help.arrayContains(movesStorage,object: array)
        {
            return true
        }
        movesStorage.append([x1,x2].sort())
        return false
    }
    func scaling(recognizer: UIPinchGestureRecognizer){
        switch recognizer.state {
        case .Changed,.Ended:
            scale *= recognizer.scale
            recognizer.scale = 1.0
        default:
            break
        }
    }
    // function that creates the background
    var backgroundRect : CGRect{
        return CGRect(x: bounds.maxX - (bounds.maxX*scale),
                      y: bounds.maxY-(bounds.maxY*scale),
                      width: -bounds.size.width+((bounds.maxX*scale)*2),
                      height: -bounds.size.height+((bounds.maxY*scale)*2)
        )
    }
    

    //Every Hill is based off of this hill with new X and Height
    private func baseHill(newX: CGFloat, newHeight: CGFloat) -> CGRect{
        return CGRect(x: newX,
                      y: bounds.maxY-(bounds.maxY*scale),
                      width: hillWidth-((hillWidth*scale)*2),
                      height: -newHeight + ((newHeight*scale)*2)
        )
        
    }
    
    private func drawHillHeights(){
        if amountOfDrops == 1{
            for y in 1...(amountOfHills){
                hillHeight.append(y)
            }
        }
        else if amountOfDrops == 2{
            for i in 1...(Int(amountOfHills/2) + 1){
                hillHeight.append(i)
            }
            for y in (1...(Int(amountOfHills/2))).reverse(){
                hillHeight.append(y)
            }
        }
        //print(hillHeight)
        hillHeight = hillHeight.shuffle()
        while checkWin == hillHeight{
            hillHeight = hillHeight.shuffle()
        }
    }
    
    //fills in the Array with all the hills
    private func drawHills(toSwitch : Int) -> Array<CGRect>{
        //print(hillPosInArray)
        var heightCount = 0
        for i in hillPosInArray{
            let convertedI = CGFloat(i)
            hills.append(
                baseHill(bounds.maxX*scale + (hillWidth - (hillWidth*scale)*2)*(convertedI*Distances.blockToBlock),
                    newHeight: CGFloat(hillHeight[heightCount]) * hillHeightConst )
            )
            heightCount = heightCount + 1
        }
        return(hills)
    }
    
    private func moveHills(toSwitch : Int){
        blockSwapAnimationDidComplete = false
        
        globalToSwitch = toSwitch
        globalSelected = selected!
        
        done = true

        checkWin = help.swapInts(checkWin,y1: selected!,y2 : toSwitch)
        usingTooManyMoves = checkIfMin(checkWin[selected!], x2: checkWin[toSwitch])
        if checkWin == hillHeight{
            won = true
        }
        
    }
    
    // meant for testing if the tap was in a hill
    private func testCollision() -> Int {
        var counter = 0
        for i in hills{
            if i.contains(point) == true{
                return(counter)
            }
            counter = counter + 1
        }
        return(-1)
    }
    
    //this is basically a Type therefore CAPATALIZE
    //this type has all the distances between everything in the level
    private struct Distances{
        static let blockToBlock: CGFloat = 2
        static let hillHeightConst: CGFloat = 20
    }
    
    func scaleUIImageToSize(let image: UIImage, let size: CGSize) -> UIImage {
        let hasAlpha = false
        let scale: CGFloat = 0.0 // Automatically use scale factor of main screen
        
        UIGraphicsBeginImageContextWithOptions(size, !hasAlpha, scale)
        image.drawInRect(CGRect(origin: CGPointZero, size: size))
        
        let scaledImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return scaledImage
    }
    
    // checks which way device is turned
    func isPortrait() -> Bool{
        if(UIDeviceOrientationIsLandscape(UIDevice.currentDevice().orientation)){
            return true
        }
        if(UIDeviceOrientationIsPortrait(UIDevice.currentDevice().orientation)){
            return false
        }
        return true
    }
    
    //Gives the scaled image of the goat
    private func drawingGoat() -> UIColor{
        let goatDrawing = UIImage(named: "goat.png")!
        
        let goatDim = bunnysTestRect
        
        let sizeOfGoat = CGSize(width:goatDim.width, height: goatDim.height)
        //print(goatDim.width,goatDim.height)
        
        var scaledGoat = scaleUIImageToSize(goatDrawing, size: sizeOfGoat)
        
        let scaledGoatColor = UIColor(patternImage: scaledGoat)
        
        return scaledGoatColor
    }
    
    private func colouringBlocks(currentBlock: CGRect) -> UIColor{
        let woodImage = UIImage(named: "barkPattern.jpg")
        
        
        //woodImage?.ani
        
        let woodColor = UIColor(patternImage: woodImage!)
        
        return woodColor
    }
    //THIS IS THE DRAW FUNCTION DO NOT EVER CALL IT
    override func drawRect(rect: CGRect) {
        // fills in the hills array with all the hills in the level
        
        
        
        let backgroundOfGame = backgroundRect
        let sizeOfGame = CGSize(width: backgroundOfGame.width, height: backgroundOfGame.height)
        
        let scaledImage = scaleUIImageToSize(backDrop, size: sizeOfGame)
        
        let scaledBackDrop = UIColor(patternImage: scaledImage)
        //Using UIBezierPath's initializer init(rect:CGRect)
        let background = UIBezierPath(rect: backgroundRect)
        
        
        //there is setfill() and setstroke(), set() does both
        UIColor.blueColor().setStroke()
        scaledBackDrop.setFill()
        background.stroke()
        background.fill()
        
        let tempCol = testCollision()
        if firstCall == true {
            for i in 0...amountOfHills-1{
                hillPosInArray.append(i)
            }
            drawHillHeights()
            hills = drawHills(tempCol)
        }
        if blockSwapAnimationDidComplete == true{
        
            if selected != nil && tempCol != -1 {
                moveHills(tempCol)
            }
            if tempCol != -1 && selected == nil{
                selected = tempCol
            }
            if tempCol == -1 || done == true {
                selected = nil
                done = false
            }
        }
            for i in 0...amountOfHills-1{
                let tempHill = UIBezierPath(rect: (hills[i]))
                color = UIColor.greenColor()
                color.setStroke()
                color = colouringBlocks(hills[i])
                color.setFill()
                if i == tempCol && selected != nil && endGame == false{
                    color = UIColor.redColor()
                    color.setStroke()
                    
                }
                tempHill.lineWidth = 1.0
                tempHill.fill()
                tempHill.stroke()
                
            }
        if firstCall{
            initialSetup = checkWin
        }
        let xPointOfFirstHill = bounds.maxX*scale
        if firstCall == true{
            bunnysTestRect = CGRect(x: xPointOfFirstHill+300,
                                    y: bounds.maxY-(bounds.maxY*scale) - hillHeightConst * CGFloat(1) - 28,
                                    width: -50,
                                    height: -50
            )
        }
        
        let bunny = UIBezierPath(rect: bunnysTestRect)
        
        UIColor.blueColor().setStroke()
        drawingGoat().setFill()
        bunny.fill()
        bunny.stroke()
        
        let xPointOfLastHill = bounds.maxX*scale + (hillWidth - (hillWidth*scale)*2)*(CGFloat(hillHeight.count)*Distances.blockToBlock)
        
        let groundRect = CGRect(x: bounds.maxX - (bounds.maxX*scale),
                              y: bounds.maxY-(bounds.maxY*scale),
                              width: -(bounds.maxX - xPointOfLastHill),
                              height: -CGFloat(initialSetup.last! + 1) * hillHeightConst
        )
        
        let ground = UIBezierPath(rect: groundRect)
        UIColor.brownColor().set()
        ground.stroke()
        ground.fill()

        firstCall = false
        //self.backgroundColor = UIColor(patternImage: UIImage(named:"gameBackground.png")!)
    }

}

