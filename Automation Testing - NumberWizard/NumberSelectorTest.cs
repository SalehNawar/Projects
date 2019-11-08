using UnityEngine;
using UnityEditor;
using UnityEngine.TestTools;
using NUnit.Framework;
using System.Collections;
/*
 ********************************************************************************************
 * Author: Saleh Nawar | 100536488                                                          *
 * Course: SoftWare Quality                                                                 *
 * Assignment - 2 : Automated Tests                                                         *
 * Discreption : This code runs automated tests that verify that the functions of Class     *
 * NumberWizard are funtioning properly.                                                    *
 *                                                                                          *
 ********************************************************************************************
 */

public class NumberSelectorTest {

    // Test case 1: insure that max = 1000
    [Test]
    public void MaxTest()
    {
        int max = 1000;
        var numberWizardMax = new NumberWizards().getMax();
     // Test if max Value of our game is equal to 1000.
        Assert.AreEqual(max, numberWizardMax); 
    }
    // Test case 2: inste that min =1
    public void MinTest()
    {
        int min = 1;
        var numberWizardMin = new NumberWizards().getMin();
        // Test if max Value of our game is equal to 1000.
        Assert.AreEqual(min, numberWizardMin);
    }
    // Test case 3: test function GuessHigher.
    [Test]

    public void GuessHigherTest()
    {
        MonoBehaviour.Instantiate(Resources.Load<GameObject>("NumberWizard"));
        var numberWizard = GameObject.Find("NumberWizard").GetComponent<NumberWizards>();
        numberWizard.GetComponent<NumberWizards>().StartGame();
        int oldGuess = numberWizard.GetComponent<NumberWizards>().getGuess();
        numberWizard.GetComponent<NumberWizards>().GuessHigher();
        int newMin = numberWizard.GetComponent<NumberWizards>().getMin();
        int newGuess = numberWizard.GetComponent<NumberWizards>().getGuess();

        Assert.AreEqual(oldGuess, newMin); // test if the new Min bound = previous guess
        Assert.GreaterOrEqual(newGuess, oldGuess); // test if new guess is higher than or equal to old guess 


    }

    // Test case 3: test GuessLower function.
    [Test]

    public void GuessLowerTest()
    {

        MonoBehaviour.Instantiate(Resources.Load<GameObject>("NumberWizard"));
        var numberWizard = GameObject.Find("NumberWizard").GetComponent<NumberWizards>();
        numberWizard.GetComponent<NumberWizards>().StartGame();
        int oldGuess = numberWizard.GetComponent<NumberWizards>().getGuess();
        numberWizard.GetComponent<NumberWizards>().GuessLower();
        int newMax= numberWizard.GetComponent<NumberWizards>().getMax();
        int newGuess = numberWizard.GetComponent<NumberWizards>().getGuess();

        Assert.AreEqual(oldGuess, newMax); // test if old guess = new max bound.
        Assert.LessOrEqual(newGuess, oldGuess); // test if new guess is lower than or equal to previous guess


    }
    //Test case 4 : test that the orginal max bound is larger than min bound and is valid 
    [Test]
	public void RangeValidityTest() {
        MonoBehaviour.Instantiate(Resources.Load<GameObject>("NumberWizard"));
        var numberWizard = GameObject.Find("NumberWizard").GetComponent<NumberWizards>();
        numberWizard.GetComponent<NumberWizards>().StartGame();
        int min = numberWizard.GetComponent<NumberWizards>().getMin(); ;
        int max = numberWizard.GetComponent<NumberWizards>().getMax();
        Assert.Greater(max, min);
	}
    //Test case 5: Automate the game to play 
    [Test]
    public void AutoPlay()
    {
       
        int guess = Random.Range(1, 1000);
        Debug.Log("I guessed " + guess);
        MonoBehaviour.Instantiate(Resources.Load<GameObject>("NumberWizard"));
        var numberWizard = GameObject.Find("NumberWizard").GetComponent<NumberWizards>();
        numberWizard.GetComponent<NumberWizards>().StartGame();
        while (guess != numberWizard.GetComponent<NumberWizards>().getGuess())
        {
            if (guess > numberWizard.GetComponent<NumberWizards>().getGuess())
            {
                numberWizard.GetComponent<NumberWizards>().GuessHigher();
            }
            if (guess < numberWizard.GetComponent<NumberWizards>().getGuess())
            {
                numberWizard.GetComponent<NumberWizards>().GuessLower();
            }


        }
       
        Assert.Pass("Gussed the number " + numberWizard.GetComponent<NumberWizards>().getGuess()); // if the computer guesses the number then pass test.  

    }

}
