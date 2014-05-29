package App;

public class Question {

	private String[] answersOptions;
	private String[] hintsList;
	private String correctAnswer;
	
	public Question(){
		
	}
	
	public boolean checkAnswer(String tryAnswer){
		return tryAnswer.equals(correctAnswer);
	}
}
