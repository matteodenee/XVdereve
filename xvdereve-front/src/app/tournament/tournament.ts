import {
  Component,
  computed,
  inject,
  signal
} from '@angular/core';

import {
  RouterLink
} from '@angular/router';

import {
  GameService
} from '../service/game';

import {
  MatchCard
} from './match-card/match-card';

import {
  MatchResultDto
} from '../type/match-result-dto';

import {
  MatchNameLabelPipe
} from '../pipe/match-name-label';

import {
  EventTypeLabelPipe
} from '../pipe/event-type-label';

import {
  DisplayNamePipe
} from '../pipe/display-name';


type TournamentPhase =
  | 'READY'
  | 'LOADING'
  | 'DRAW'
  | 'MATCH'
  | 'MATCH_END'
  | 'FINISHED';


@Component({
  selector: 'app-tournament',

  imports: [
    RouterLink,
    MatchCard,
    MatchNameLabelPipe,
    EventTypeLabelPipe,
    DisplayNamePipe
  ],

  templateUrl: './tournament.html',
  styleUrl: './tournament.css'
})
export class Tournament {

  readonly gameService =
    inject(GameService);


  readonly gameState =
    this.gameService.gameState;


  readonly result =
    this.gameService.tournamentResult;


  readonly loading =
    signal(false);


  readonly error =
    signal<string | null>(null);


  /*
   * Etat actuel de l'animation du tournoi.
   */
  readonly phase =
    signal<TournamentPhase>('READY');


  /*
   * Index du match actuellement affiché.
   */
  readonly currentMatchIndex =
    signal(0);


  /*
   * Nombre d'événements actuellement révélés.
   */
  readonly visibleEventCount =
    signal(0);


  /*
   * Permet de faire apparaître l'adversaire
   * après un petit temps de "tirage".
   */
  readonly drawRevealed =
    signal(false);


  /*
   * Match actuellement joué.
   */
  readonly currentMatch =
    computed<MatchResultDto | null>(() => {

      const tournament =
        this.result();

      if (!tournament) {
        return null;
      }

      return (
        tournament.matches[
          this.currentMatchIndex()
        ] ?? null
      );

    });


  /*
   * Evénements déjà révélés.
   */
  readonly visibleEvents =
    computed(() => {

      const match =
        this.currentMatch();

      if (!match) {
        return [];
      }

      return match.events.slice(
        0,
        this.visibleEventCount()
      );

    });


  /*
   * Même liste, mais l'événement le plus récent
   * apparaît en premier.
   */
  readonly visibleEventsNewestFirst =
    computed(() => {

      return [
        ...this.visibleEvents()
      ].reverse();

    });


  /*
   * Score actuel de notre équipe.
   *
   * Pendant le match, on n'utilise surtout pas
   * directement le score final du DTO.
   */
  readonly liveScoreMyTeam =
    computed(() => {

      const events =
        this.visibleEvents();

      if (events.length === 0) {
        return 0;
      }

      return events[
        events.length - 1
      ].scoreMyTeam;

    });


  /*
   * Score actuel de l'adversaire.
   */
  readonly liveScoreOpponent =
    computed(() => {

      const events =
        this.visibleEvents();

      if (events.length === 0) {
        return 0;
      }

      return events[
        events.length - 1
      ].scoreAdverseTeam;

    });


  /*
   * Numéro du match pour l'affichage.
   */
  readonly displayedMatchNumber =
    computed(() => {

      return (
        this.currentMatchIndex() + 1
      );

    });


  /*
   * Nombre total de matchs dans le tournoi.
   */
  readonly totalMatches =
    computed(() => {

      return (
        this.result()?.matches.length ?? 0
      );

    });


  /*
   * Pourcentage de progression du tournoi.
   */
  readonly tournamentProgress =
    computed(() => {

      const total =
        this.totalMatches();

      if (total === 0) {
        return 0;
      }

      return (
        (
          this.currentMatchIndex()
          / total
        )
        * 100
      );

    });


  /*
   * Titre du bilan final.
   */
  readonly statusTitle =
    computed(() => {

      const result =
        this.result();

      if (!result) {
        return '';
      }

      if (result.champion) {
        return 'Champion du monde !';
      }

      if (result.qualified) {
        return 'Éliminé en phase finale';
      }

      return 'Éliminé en phase de poules';

    });


  /*
   * Lancement du tournoi.
   */
  simulate(): void {

    if (this.loading()) {
      return;
    }

    this.loading.set(true);

    this.error.set(null);

    this.phase.set('LOADING');


    this.gameService
      .simulateTournament()
      .subscribe({

        next: () => {

          this.loading.set(false);

          this.currentMatchIndex.set(0);

          this.visibleEventCount.set(0);

          void this.playCurrentMatch();

        },

        error: () => {

          this.loading.set(false);

          this.phase.set('READY');

          this.error.set(
            'Impossible de simuler le tournoi.'
          );

        }

      });

  }


  /*
   * Déroulement complet d'un match.
   *
   * 1. Tirage
   * 2. Révélation adversaire
   * 3. Match
   * 4. Evénements
   * 5. Résultat
   * 6. Match suivant
   */
  private async playCurrentMatch():
    Promise<void> {

    const match =
      this.currentMatch();

    if (!match) {

      this.phase.set('FINISHED');

      return;

    }


    /*
     * TIRAGE
     */

    this.visibleEventCount.set(0);

    this.drawRevealed.set(false);

    this.phase.set('DRAW');


    await this.wait(700);


    /*
     * Révélation de l'adversaire.
     */

    this.drawRevealed.set(true);


    await this.wait(1600);


    /*
     * DEBUT DU MATCH
     */

    this.phase.set('MATCH');


    await this.wait(900);


    /*
     * Les événements apparaissent
     * progressivement.
     */

    for (
      let i = 0;
      i < match.events.length;
      i++
    ) {

      this.visibleEventCount.set(
        i + 1
      );


      await this.wait(950);

    }


    /*
     * Petit délai après le dernier événement.
     */

    await this.wait(700);


    /*
     * FIN DU MATCH
     */

    this.phase.set('MATCH_END');


    await this.wait(2200);


    /*
     * Passage au match suivant.
     */

    const tournament =
      this.result();


    if (!tournament) {

      this.phase.set('FINISHED');

      return;

    }


    const nextMatchIndex =
      this.currentMatchIndex() + 1;


    if (
      nextMatchIndex
      < tournament.matches.length
    ) {

      this.currentMatchIndex.set(
        nextMatchIndex
      );

      await this.playCurrentMatch();

      return;

    }


    /*
     * FIN DU TOURNOI
     */

    this.phase.set('FINISHED');

  }


  /*
   * Petite fonction utilitaire permettant
   * de temporiser les animations.
   */
  private wait(
    milliseconds: number
  ): Promise<void> {

    return new Promise(
      resolve => {

        setTimeout(
          resolve,
          milliseconds
        );

      }
    );

  }

}