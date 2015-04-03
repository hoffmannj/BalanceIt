package com.hofi.game.balance.screens;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.hofi.game.balance.CharMap;
import com.hofi.game.balance.Common;
import com.hofi.game.balance.NewButton;
import com.hofi.game.balance.PlayerInfo;
import com.hofi.game.balance.TextWriter;
import com.hofi.game.balance.interfaces.GameScreen;
import com.hofi.game.balance.interfaces.NewButtonHandler;

public class Leaderboard implements GameScreen {
	protected CharMap charMap;
	private final String strName = "Leaderboard";
	private ArrayList<PlayerInfo> scores;
	private boolean scoresReceived = false;
	private TextWriter textWriter;

	@Override
	public void TouchStart() {

	}

	@Override
	public void TouchEnd() {

	}

	@Override
	public void TouchMove() {

	}

	@Override
	public String getName() {
		return strName;
	}

	private void getLeaderboard() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				PendingResult<Leaderboards.LoadScoresResult> result = Games.Leaderboards
						.loadTopScores(Common.mainActivity.getApiClient(),
								Common.leaderboard_ID,
								LeaderboardVariant.TIME_SPAN_ALL_TIME,
								Common.currentLeaderboardType, 10);
				if (result != null) {
					try {
						result.setResultCallback(new ResultCallback<LoadScoresResult>() {

							@Override
							public void onResult(LoadScoresResult arg0) {
								if (arg0 == null)
									return;
								LeaderboardScoreBuffer buff = arg0.getScores();
								if (buff == null) {
									arg0.release();
									return;
								}
								int count = buff.getCount();
								synchronized (scores) {
									scoresReceived = true;
									scores.clear();
									for (int i = 0; i < count; i++) {
										LeaderboardScore lbs = buff.get(i);
										if (lbs == null)
											continue;
										PlayerInfo pi = new PlayerInfo();
										pi.setName(lbs
												.getScoreHolderDisplayName());
										pi.setScore(lbs.getDisplayScore());
										scores.add(pi);
									}
								}
								buff.close();
								arg0.release();
							}

						});
					} catch (NullPointerException ex) {

					}
				}
			}

		}).run();
	}

	@Override
	public void init() {
		charMap = new CharMap();
		scores = new ArrayList<PlayerInfo>();

		textWriter = new TextWriter(32);

		if (Common.mainActivity.isSignedIn()) {
			getLeaderboard();
		}

		String txtchglb = "Show Public";
		if (Common.currentLeaderboardType == LeaderboardVariant.COLLECTION_PUBLIC)
			txtchglb = "Show Social";

		Common.addNewButton(this, new Vec2(120, 750), txtchglb,
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						int n = LeaderboardVariant.COLLECTION_PUBLIC;
						if (Common.currentLeaderboardType == LeaderboardVariant.COLLECTION_PUBLIC)
							n = LeaderboardVariant.COLLECTION_SOCIAL;
						Common.currentLeaderboardType = n;
						Common.gamePlayManager.forceScreenChange();
						Common.gamePlayManager.setCurrentScreen("leaderboard");
					}
				});

		Common.addNewButton(this, new Vec2(420, 750), "Back",
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						Common.gamePlayManager.setCurrentScreen("mainmenu");
					}
				});

		Common.addNewButton(this, new Vec2(240, 55), "Show Google Leaderboard",
				new NewButtonHandler() {
					@Override
					public void handle(NewButton sender, Object owner) {
						Common.mainActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Common.getIsSignedIn();
								Common.getIsSignedIn();
								if (Common.isSignedIn) {
									Common.mainActivity.startActivityForResult(
											Games.Leaderboards.getLeaderboardIntent(
													Common.mainActivity
															.getApiClient(),
													Common.leaderboard_ID),
											Common.REQUEST_LEADERBOARD);
								}
							}

						});
						Common.gamePlayManager.forceScreenChange();
						Common.gamePlayManager.setCurrentScreen("leaderboard");
					}
				});
	}

	@Override
	public void initFromGameState() {

	}

	@Override
	public void draw() {
		synchronized (scores) {
			if (!scoresReceived) {
				charMap.drawString("Fetching the list...", 20, 400);
			} else {
				int count = scores.size();
				if (count == 0) {
					charMap.drawString("There are no scores yet", 15, 20, 400,
							1, 1, 1, 1);
				} else {
					int y = 660;
					for (int i = 0; i < count; i++) {
						PlayerInfo pi = scores.get(i);
						textWriter.writeText(pi.getScore(), 20, y,
								TextWriter.TextAlign.Left);
						textWriter.writeText(pi.getName(), 160, y,
								TextWriter.TextAlign.Left);
						y -= 35;
					}
				}
			}
		}

		Common.uiObjects.draw();
	}

	@Override
	public void update() {
	}

	@Override
	public void updateGameState(boolean saveScore) {
		Common.gameState.screenName = "mainmenu";
	}

	@Override
	public void cleanUp() {

	}
}
