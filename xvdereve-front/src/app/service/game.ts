import {
  inject,
  Injectable,
  signal
} from '@angular/core';

import {
  HttpClient
} from '@angular/common/http';

import {
  tap
} from 'rxjs';

import {
  GameStateDto
} from '../type/game-state-dto';

import {
  TournamentResultDto
} from '../type/tournament-result-dto';


@Injectable({
  providedIn: 'root'
})
export class GameService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    '/api/game';


  private readonly _gameState =
    signal<GameStateDto | null>(null);

  readonly gameState =
    this._gameState.asReadonly();


  private readonly _tournamentResult =
    signal<TournamentResultDto | null>(null);

  readonly tournamentResult =
    this._tournamentResult.asReadonly();


  startGame() {

    /*
     * Une nouvelle partie efface
     * forcément l'ancien tournoi.
     */
    this._tournamentResult.set(null);

    return this.http
      .post<GameStateDto>(
        `${this.apiUrl}/start`,
        {}
      )
      .pipe(
        tap(state =>
          this._gameState.set(state)
        )
      );
  }


  getState() {

    return this.http
      .get<GameStateDto>(
        `${this.apiUrl}/state`
      )
      .pipe(
        tap(state =>
          this._gameState.set(state)
        )
      );
  }


  respin() {

    return this.http
      .post<GameStateDto>(
        `${this.apiUrl}/respin`,
        {}
      )
      .pipe(
        tap(state =>
          this._gameState.set(state)
        )
      );
  }


  pickPlayer(playerId: number) {

    return this.http
      .post<GameStateDto>(
        `${this.apiUrl}/pick`,
        {
          playerId
        }
      )
      .pipe(
        tap(state =>
          this._gameState.set(state)
        )
      );
  }


  chooseKicker(playerId: number) {

    return this.http
      .post<GameStateDto>(
        `${this.apiUrl}/kicker`,
        {
          playerId
        }
      )
      .pipe(
        tap(state =>
          this._gameState.set(state)
        )
      );
  }


  simulateTournament() {

    return this.http
      .post<TournamentResultDto>(
        `${this.apiUrl}/simulate`,
        {}
      )
      .pipe(
        tap(result =>
          this._tournamentResult.set(result)
        )
      );
  }

}