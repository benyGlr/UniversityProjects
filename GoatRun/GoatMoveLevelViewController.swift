//
//  GoatMoveLevelViewController.swift
//  GoatRun
//
//  Created by Sam Jones on 2016-08-19.
//  Copyright © 2016 Benjamin. All rights reserved.
//

import UIKit

class GoatMoveLevelViewController: LevelViewController {
    
    var dx = 0
    var dy = 0
    
    var moving: Bool = false {
        didSet{
            startJumping()
        }
    }
    
    @IBAction func submitAnswer(sender: UIButton) {
        checkWon()
    }
    
    func checkWon(){
        //print(endGame)
        if levelView.won == true{
            if gameText.text == "..."{
                gameText.text = "winner"
            }
            if gameText == "Used Too Many Moves"{
                gameText.text = "Next time try to use less moves"
            }
            levelView.endGame = true
            moving = true
        }
        
    }
    
    // NSTimer repeats
    var timerCountSeconds = 0
    // full jump cycles
    var jumps = 0
    
    func startJumping(){
        if moving{
                NSTimer.scheduledTimerWithTimeInterval(
                0.001,
                target: self,
                selector: #selector(GoatMoveLevelViewController.jumpingParabola(_:)),
                userInfo: nil,
                repeats: true
        )
        }
    }
    
    func jumpingParabola(timer: NSTimer){
        dx = dx + 1
        dy = dx^2 + dx*2 - 21
        levelView.bunnysTestRect.offsetInPlace(dx: CGFloat(dx), dy: CGFloat(dy))
        timerCountSeconds = timerCountSeconds + 1
        if timerCountSeconds >= 11{
            dx = 0
            timerCountSeconds = 0
            jumps = jumps + 1
        }
        if jumps == levelView.amountOfHills{
            timer.invalidate()
        }
    }
    
    // find a way to replace checkLoss since its super inefficient
    // this function calls the function that checks whether or not you lost every few seconds
    func checkLoss(){
        NSTimer.scheduledTimerWithTimeInterval(
            0.0005,
            target: self,
            selector: #selector(GoatMoveLevelViewController.updateLoss(_:)),
            userInfo: nil,
            repeats: true
        )

    }
    
    func updateLoss(timer: NSTimer){
        if levelView.endGame == true{
            timer.invalidate()
        }
        if levelView.usingTooManyMoves == true{
            gameText.text = "Used Too Many Moves"
        }
    }
    
}
