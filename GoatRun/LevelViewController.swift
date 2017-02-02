//
//  ViewController.swift
//  GoatRun
//
//  Created by Sam Jones on 2016-07-18.
//  Copyright © 2016 Benjamin. All rights reserved.
//

import UIKit

class LevelViewController: UIViewController {
    
    var levelAssests = LevelSetter(level: .One){
        didSet {
            updateUI()
        }
    }
    
    
    
    @IBOutlet weak var gameText: UILabel!
    
    @IBOutlet weak var levelView: LevelView!{
        didSet
        {
            levelView.addGestureRecognizer(UIPinchGestureRecognizer(target: levelView, action: #selector(levelView.scaling(_:))
                ))
            
            levelView.addGestureRecognizer(UITapGestureRecognizer(target: levelView, action: #selector(LevelView.swap(_:))
                ))
                    
            updateUI()
        }
    }
    
    func animationSetup(){
        let backGroundLayer = CALayer()
        
        backGroundLayer.frame = levelView.backgroundRect
        backGroundLayer.backgroundColor = UIColor.redColor().CGColor
        self.view.layer.addSublayer(backGroundLayer)
    }
    
    //These 2 functions together stop the app from rotating
    override func shouldAutorotate() -> Bool {
        return true
    }
    
    override func supportedInterfaceOrientations() -> UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.Landscape
    }
   
    //handles the win game case
    
    
    private func updateUI(){
        switch levelAssests.level {
            case .One: (levelView.amountOfHills = 5,
                        levelView.scale = 0.0,
                        levelView.checkWin = [1,2,3,4,5],
                        levelView.amountOfDrops = 1)
            
            case .Two: (levelView.amountOfHills = 5,
                        levelView.scale = 0.0,
                        levelView.checkWin = [1,2,3,2,1],
                        levelView.amountOfDrops = 2
            )
            
        }
        
        //print("updating")
        
    }


}

