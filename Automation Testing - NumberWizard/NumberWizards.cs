using UnityEngine;
using System.Collections;
using UnityEngine.UI;

/*
 ********************************************************************************************
 * Author: Saleh Nawar | 100536488                                                          *
 * Course: SoftWare Quality                                                                 *
 * Assignment - 2 : NumberWizard Class                                                      *
 * Description : This code controls the game behavior. The basic idea is to set min and max *
 * values and try to guess a number in between.                                             *
 *                                                                                          *
 ********************************************************************************************
 */
public class NumberWizards : MonoBehaviour {

    // Variables
	public Text guessText;
	int max = 1000;
	int min = 1;
	int guess;

    //Getters
    public int getMin() {
        return min;
    }

    public int getMax(){
        return max;
    }
    public int getGuess(){
        return guess;
    }

    // Start funtion -- standard unity funtion.
	void Start () {
		StartGame();
	}
	//start game. 
	public void StartGame () {
		max = max + 1; // we add one to prevent a bug.
		NextGuess();
	}
    // This function sets the lower bound as curent guess and then guesses a new number between the new bounds.
	public void GuessHigher(){
		min = guess;
		NextGuess();
	}
    // This function sets the upper bound as the max bound and then guesses a new number between the new bounds.
	public void GuessLower(){
		max = guess;
		NextGuess ();
	}
    // if User press equal button then restart game.
	public void GuessCorrect(){
		StartGame ();
	}
	// make a new random guess between min and max and output to screen.
	void NextGuess () {
		guess = Random.Range(min, max); // using random method to guess a new number.
		print ("Next guess is " + guess); // print to console.
		guessText.text = guess.ToString(); // print to screen.
	}
}
