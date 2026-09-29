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


type AnimationSpeed =
  | 1
  | 2
  | 4
  | 0;


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


  readonly phase =
    signal<TournamentPhase>('READY');


  readonly currentMatchIndex =
    signal(0);


  readonly visibleEventCount =
    signal(0);


  readonly drawRevealed =
    signal(false);


  /*
   * 1 = vitesse normale
   * 2 = deux fois plus rapide
   * 4 = quatre fois plus rapide
   * 0 = résultat instantané
   */
  readonly animationSpeed =
    signal<AnimationSpeed>(1);


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


  readonly visibleEventsNewestFirst =
    computed(() => {

      return [
        ...this.visibleEvents()
      ].reverse();

    });


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


  readonly displayedMatchNumber =
    computed(() => {

      return (
        this.currentMatchIndex() + 1
      );

    });


  readonly totalMatches =
    computed(() => {

      return (
        this.result()?.matches.length ?? 0
      );

    });


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

          /*
           * Si le joueur avait déjà choisi
           * "résultat instantané" pendant
           * le chargement.
           */
          if (
            this.animationSpeed() === 0
          ) {

            this.showFinalResult();

            return;
          }

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
   * Change la vitesse à n'importe quel
   * moment du tournoi.
   */
  setSpeed(
    speed: AnimationSpeed
  ): void {

    this.animationSpeed.set(speed);


    /*
     * Si le résultat du backend existe déjà
     * et que le joueur demande l'instantané,
     * on affiche directement le bilan.
     */
    if (
      speed === 0
      &&
      this.result()
    ) {

      this.showFinalResult();

    }

  }


  private showFinalResult(): void {

    const tournament =
      this.result();

    if (!tournament) {
      return;
    }


    /*
     * On place l'index sur le dernier match
     * pour garder un état cohérent.
     */
    if (
      tournament.matches.length > 0
    ) {

      this.currentMatchIndex.set(
        tournament.matches.length - 1
      );

      const lastMatch =
        tournament.matches[
          tournament.matches.length - 1
        ];

      this.visibleEventCount.set(
        lastMatch.events.length
      );

    }


    this.drawRevealed.set(true);

    this.phase.set('FINISHED');

  }


  private async playCurrentMatch():
    Promise<void> {

    /*
     * Permet de couper immédiatement
     * l'animation.
     */
    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    const match =
      this.currentMatch();

    if (!match) {

      this.phase.set('FINISHED');

      return;

    }


    /*
     * ===========================
     * TIRAGE
     * ===========================
     */

    this.visibleEventCount.set(0);

    this.drawRevealed.set(false);

    this.phase.set('DRAW');


    await this.wait(700);


    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    /*
     * Révélation de l'adversaire.
     */

    this.drawRevealed.set(true);


    await this.wait(1600);


    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    /*
     * ===========================
     * DEBUT DU MATCH
     * ===========================
     */

    this.phase.set('MATCH');


    await this.wait(900);


    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    /*
     * ===========================
     * EVENEMENTS
     * ===========================
     */

    for (
      let i = 0;
      i < match.events.length;
      i++
    ) {

      if (
        this.animationSpeed() === 0
      ) {

        this.showFinalResult();

        return;

      }


      this.visibleEventCount.set(
        i + 1
      );


      await this.wait(950);

    }


    await this.wait(700);


    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    /*
     * ===========================
     * FIN DU MATCH
     * ===========================
     */

    this.phase.set('MATCH_END');


    await this.wait(2200);


    if (
      this.animationSpeed() === 0
    ) {

      this.showFinalResult();

      return;

    }


    /*
     * ===========================
     * MATCH SUIVANT
     * ===========================
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
     * ===========================
     * FIN DU TOURNOI
     * ===========================
     */

    this.phase.set('FINISHED');

  }


  /*
   * Attente dynamique.
   *
   * L'intérêt par rapport à un simple :
   *
   * setTimeout(ms / vitesse)
   *
   * est que la vitesse peut être modifiée
   * PENDANT l'attente.
   *
   * Exemple :
   *
   * x1 -> x4 pendant le tirage
   *
   * l'animation accélère immédiatement.
   */
  private async wait(
    baseMilliseconds: number
  ): Promise<void> {

    let elapsed =
      0;


    let previousTime =
      performance.now();


    while (
      elapsed < baseMilliseconds
    ) {

      /*
       * Résultat instantané.
       */
      if (
        this.animationSpeed() === 0
      ) {

        return;

      }


      await new Promise<void>(
        resolve => {

          setTimeout(
            resolve,
            40
          );

        }
      );


      const now =
        performance.now();


      const realElapsed =
        now - previousTime;


      previousTime =
        now;


      /*
       * x1 :
       * 40 ms réelles = 40 ms animation
       *
       * x2 :
       * 40 ms réelles = 80 ms animation
       *
       * x4 :
       * 40 ms réelles = 160 ms animation
       */
      elapsed +=
        realElapsed
        *
        this.animationSpeed();

    }

  }

}