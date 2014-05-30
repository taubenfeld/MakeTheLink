package App;

public class Question {

	private String[] answersOptions;
	private String[] hintsList;
	private String correctAnswer;
	
	public void setAnswer(String answer){
		correctAnswer=answer;
	}
	
	public void setAnswerOptions(String[] answerOps){
		answersOptions=answerOps;
	}
	
	public void setHintsList(String[] hintLst){
		hintsList=hintLst;
	}
	
	public String getAnswer(String answer){
		return correctAnswer;
	}
	
	public String[] getAnswerOptions(String[] answerOps){
		return answersOptions;
	}
	
	public String[] getHintsList(String[] hintLst){
		return hintsList;
	}
	
	public Question(){
		
	}
	
	public boolean checkAnswer(String tryAnswer){
		return tryAnswer.equals(correctAnswer);
	}
}
