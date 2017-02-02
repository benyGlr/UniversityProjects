//
//  File.swift
//  GoatRun
//
//  Created by Sam Jones on 2016-08-22.
//  Copyright © 2016 Benjamin. All rights reserved.
//

import Foundation
import UIKit

class HelperClass{
    
    func swapRects(array : Array<CGRect>, y1 : Int, y2 : Int) -> Array<CGRect>{
        var x = array
        if y1 != y2{
            swap(&x[y1], &x[y2])
        }
        return x
    }
    
    func swapInts(array : Array<Int>, y1 : Int, y2 : Int) -> Array<Int>{
        var x = array
        if y1 != y2{
            swap(&x[y1], &x[y2])
        }
        return x
    }
    
    func arrayContains(array: Array<Array<Int>>, object: Array<Int>) -> Bool
    {
        for i in array{
            if i == object{
                return true
            }
        }
        
        return false
    }
}
