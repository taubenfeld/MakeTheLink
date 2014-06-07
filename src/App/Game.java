package App;

import DatabaseConnection.databaseConnection;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Game {
	private int DifficultLevel;
	private int NumOfRounds;
	private int curRoundNum = 1;
	private Map<String, Integer> CategoryMap;
	private Map<String, Integer> playerNameAndScore = new HashMap();
	private databaseConnection thisConnection;
	private Question[] thisQuestions;
	private Question curQuestion;

	public Game(int DifficultLevel, int NumOfRounds,
			Map<String, Integer> CategoryMap, String[] PlayersName)
			throws ClassNotFoundException, SQLException, IOException,
			PropertyVetoException {
		this.DifficultLevel = DifficultLevel;
		this.NumOfRounds = NumOfRounds;
		this.CategoryMap = CategoryMap;
		for (String player : PlayersName) {
			this.playerNameAndScore.put(player, Integer.valueOf(0));
		}
		this.thisConnection = new databaseConnection();
		this.thisConnection.setQuestionOps(this.CategoryMap,
				this.DifficultLevel);
		this.thisQuestions = databaseConnection.genrateQuestion(NumOfRounds);
		this.curQuestion = this.thisQuestions[0];
	}

	public boolean checkAnswerAndUpdate(String playerName, String theAnswer,
			int clockTime) {
		boolean isRightAnswer = this.curQuestion.checkAnswer(theAnswer);
		if (isRightAnswer) {
			updateScore(playerName, clockTime, true);
			moveToNextRound();
		} else {
			updateScore(playerName, clockTime, false);
		}
		return isRightAnswer;
	}

	public void updateScore(String playerName, int timeLeft, boolean correct) {
		if (correct) {
			this.playerNameAndScore.put(playerName,
					Integer.valueOf(((Integer) this.playerNameAndScore
							.get(playerName)).intValue() + 1));
		} else {
			this.playerNameAndScore.put(playerName,
					Integer.valueOf(((Integer) this.playerNameAndScore
							.get(playerName)).intValue() - 1));
		}
	}

	public void moveToNextRound() {
		this.curRoundNum += 1;
		if (curRoundNum <= NumOfRounds){
			this.curQuestion = this.thisQuestions[this.curRoundNum-1];
		}
		
	}

	public int calcScoreByTime(int timeLeft) {
		return timeLeft;
	}

	public Question getThisQuestion() {
		return this.curQuestion;
	}

	public int getCurRoundNum() {
		return this.curRoundNum;
	}

	public Map<String, Integer> getPlayerNameAndScore() {
		return this.playerNameAndScore;
	}

	public int getNumOfRounds() {
		return this.NumOfRounds;
	}

	public int getDifficultLevel() {
		return this.DifficultLevel;
	}

	public Map<String, Integer> getCategoryMap() {
		return this.CategoryMap;
	}
}
