/*
 * Copyright 2018, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.SimpleCalc;

/**
 * Utility class for SimpleCalc to perform the actual calculations.
 */
public class Calculator {

    // Available operations
    public enum Operator {ADD, SUB, DIV, MUL}

    /**
     * Addition operation
     */
    public double add(double firstOperand, double secondOperand) {

        if(firstOperand==0.0 && secondOperand==0.0)
            return 0.0;

        double result = firstOperand + secondOperand;

        overflowUnderflowCheck(result);

        return result;
    }


    /**
     * Subtract operation
     */
    public double sub(double firstOperand, double secondOperand) {

        if(firstOperand == secondOperand)
            return 0.0;

        double result = firstOperand - secondOperand;

        overflowUnderflowCheck(result);

        return firstOperand - secondOperand;
    }

    /**
     * Divide operation
     */
    public double div(double firstOperand, double secondOperand) {

        if(firstOperand==0.0 && secondOperand!=0.0)
            return 0.0;

        double result = firstOperand/secondOperand;

        overflowUnderflowCheck(result);

        return result;
    }

    /**
     * Multiply operation
     */
    public double mul(double firstOperand, double secondOperand) {

        if(firstOperand==0 || secondOperand==0)
            return 0.0;

        double result = firstOperand*secondOperand;

        overflowUnderflowCheck(result);

        return result;
    }

    private void overflowUnderflowCheck(double result) {
    // Java doesn't have a overthrow/underflow class
    // have to manually detect overflow/underflow

        // courtesy- https://www.baeldung.com/java-overflow-underflow#detecting-overflow-underflow-floating-point
        if(result == Double.POSITIVE_INFINITY
        || result == Double.NEGATIVE_INFINITY
        || Double.compare(-0.0f, result) == 0
        || Double.compare(+0.0f, result) == 0
        || Double.compare(Double.NaN, result) == 0)
            throw new ArithmeticException("ERROR! overflow/underflow detected");
    }
}