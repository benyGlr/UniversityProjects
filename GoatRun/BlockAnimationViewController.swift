//
//  BlockAnimationViewController.swift
//  GoatRun
//
//  Created by Sam Jones on 2016-08-27.
//  Copyright © 2016 Benjamin. All rights reserved.
//

import UIKit

class BlockAnimationViewController: GoatMoveLevelViewController {
    
    var timerCountSecondsForBlocks = 0
    
    var a = CGFloat(0.0)
    
    var b = CGFloat(0.0)
    
    var swapping = false {
        didSet{
            if swapping == true
            {
                swapAnimation()
            }
        }
    }
    
    func callCheckIfSwapNeeded(){
        NSTimer.scheduledTimerWithTimeInterval(
            0.0001,
            target: self,
            selector: #selector(BlockAnimationViewController.checkIfSwapNeeded(_:)),
            userInfo: nil,
            repeats: true
        )
    }
    
    func checkIfSwapNeeded(timer: NSTimer){
        if levelView.endGame == true{
            timer.invalidate()
        }
        
        if levelView.blockSwapAnimationDidComplete == false{
            timer.invalidate()
            swapAnimation()
        }
    }
    
    func swapAnimation(){
        a = levelView.hills[levelView.globalToSwitch].maxX
        b = levelView.hills[levelView.globalSelected].maxX
        
        NSTimer.scheduledTimerWithTimeInterval(
            0.00001,
            target: self,
            selector: #selector(BlockAnimationViewController.swapHills(_:)),
            userInfo: nil,
            repeats: true
        )
    }
    
    
    func swapHills(timer: NSTimer){
        //hills[selected!].offsetInPlace(dx: a - b, dy: 0.0)
        //hills[toSwitch].offsetInPlace(dx: b - a, dy: 0.0)
        
        
        
        if timerCountSecondsForBlocks < 30{
            levelView.hills[levelView.globalToSwitch].offsetInPlace(dx: CGFloat(0), dy: -CGFloat(2))
            levelView.hills[levelView.globalSelected].offsetInPlace(dx: CGFloat(0), dy: -CGFloat(2))
            timerCountSecondsForBlocks = timerCountSecondsForBlocks + 1
            
        }
        else if levelView.hills[levelView.globalSelected].maxX != a{
            levelView.hills[levelView.globalSelected].offsetInPlace(dx: abs((a-b))/(a-b), dy: 0.0)
            levelView.hills[levelView.globalToSwitch].offsetInPlace(dx: abs((b-a))/(b-a), dy: 0.0)
        }
        else if timerCountSecondsForBlocks < 60{
            levelView.hills[levelView.globalToSwitch].offsetInPlace(dx: CGFloat(0), dy: CGFloat(2))
            levelView.hills[levelView.globalSelected].offsetInPlace(dx: CGFloat(0), dy: CGFloat(2))
            timerCountSecondsForBlocks = timerCountSecondsForBlocks + 1
            
        }
        else if timerCountSecondsForBlocks == 60{
            swapping = false
            timerCountSecondsForBlocks = 0
            timer.invalidate()
            levelView.blockSwapAnimationDidComplete = true
            callCheckIfSwapNeeded()
        }
        
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        //this will make it start instantly (for testing only)
        checkLoss()
        callCheckIfSwapNeeded()
        
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        moving = false
    }


}
