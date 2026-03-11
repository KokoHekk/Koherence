import java.util.Random;
import java.util.Scanner;

/**
 * Koherence story - 8 unique days building to bad ending.
 */
public class Lore {

    private static final Random rand = new Random();

    /**
     * Main day method - has unique activity per day.
     * Advances days sequentially unless caretakerReset() is called.
     */
    public static void runDay(Scanner scanner, main.GameState state) {
        int dayNum = state.currentDay;
        int resets = ResetCounter.getInstance().getResetCount();
        String timePeriod = getRandomTimePeriod(dayNum, resets);

        System.out.println("\n=== Day " + (dayNum + 1) + " - " + timePeriod + " ===");

        wakeUpScene(scanner, state, dayNum, resets);

        switch (dayNum) {
            case 0 -> day1Cleaning(scanner, state);
            case 1 -> day2CookingLesson(scanner, state);  // Day 2
            case 2 -> day3KnifeTraining(scanner, state);  // Day 3
            case 3 -> day4PaintballPractice(scanner, state);
            case 4 -> day5LaundryStories(scanner, state);
            case 5 -> day6BalconyReflection(scanner, state);
            case 6 -> day7GuestPrep(scanner, state);
            case 7 -> day8BadEnding(scanner, state);
            default -> {
                System.out.println("Day progression error: " + dayNum);
                state.badEndingReached = true;
            }
        }

        if (!state.badEndingReached) {
            completeDayTransition(state, dayNum, resets);
        }
    }



    /**
     * Wake up scene with day specific morning dialog.
     */
    private static void wakeUpScene(Scanner scanner, main.GameState state, int dayNum, int resets) {
        String morningSensationKey = switch (dayNum) {
            case 0 -> "wake.morning.day0";
            case 1 -> "wake.morning.day1";
            case 2 -> "wake.morning.day2";
            case 3 -> "wake.morning.day3";
            case 4 -> "wake.morning.day4";
            case 5 -> "wake.morning.day5";
            case 6 -> "wake.morning.day6";
            case 7 -> "wake.morning.day7";
            default -> "wake.morning.default";
        };
        System.out.println("\n" + VerbParser.getMessage(morningSensationKey));

        String dejaVu = Functions.subtleDejaVuSupplier().get();
        if (!dejaVu.isEmpty()) {
            System.out.println("\n" + dejaVu);
        }

        // GameBoy is available every morning until picked up once
        if (!state.hasGameBoy) {
            System.out.println(VerbParser.getMessage("wake.gameboy.prompt"));
            String takeChoice = scanner.nextLine().trim().toLowerCase();
            if (takeChoice.startsWith("y")) {
                state.hasGameBoy = true;
                System.out.println("GameBoy tucked close. Tail flicks with comfort.");
                main.saveGameState(state);
            }
        }

        String caretakerMood = Functions.caretakerMoodSupplier().get();
        System.out.println(VerbParser.getMessage("wake.door.opens") + caretakerMood);

        String morningDialogKey = switch (dayNum) {
            case 0 -> "wake.morning.dialog.day0";
            case 1 -> "wake.morning.dialog.day1";
            case 2 -> "wake.morning.dialog.day2";
            case 3 -> "wake.morning.dialog.day3";
            case 4 -> "wake.morning.dialog.day4";
            case 5 -> "wake.morning.dialog.day5";
            case 6 -> "wake.morning.dialog.day6";
            case 7 -> "wake.morning.dialog.day7";
            default -> "wake.morning.dialog.default";
        };
        System.out.println(VerbParser.getMessage(morningDialogKey));
    }


    /**
     * Helps advance the player to next day.
     */
    private static void completeDayTransition(main.GameState state, int dayNum, int resets) {
        if (state.wasResetThisTurn) {
            state.wasResetThisTurn = false;
            main.saveGameState(state);
            main.logger.info("Evening + key decision skipped (reset)");
            return;
        }

        if (state.hasKey && dayNum < 7) {
            System.out.println(VerbParser.getMessage("transition.key.heavy"));
            System.out.println(VerbParser.getMessage("transition.master.door"));
            System.out.println(VerbParser.getMessage("transition.try.tonight"));

            String tryKey = new Scanner(System.in).nextLine().trim().toLowerCase();
            if (tryKey.startsWith("y")) {
                System.out.println(VerbParser.getMessage("transition.slip.downstairs"));
                masterBedroomEntry(new Scanner(System.in), state);
                return;
            }
        }

        String transition = switch (resets) {
            case 0 -> VerbParser.getMessage("transition.evening.reset0");
            case 1, 2 -> VerbParser.getMessage("transition.evening.reset12");
            default -> VerbParser.getMessage("transition.evening.default");
        };

        System.out.println(transition);
        System.out.println(VerbParser.getMessage("transition.night.falls"));

        state.currentDay++;
        main.saveGameState(state);
        main.logger.info("Day " + (dayNum + 1) + " completed → Day " + (state.currentDay + 1));
    }


    /**
     * Day 1: Cleaning (has multiple rooms, and potential key discovery for good ending)
     */
    private static void day1Cleaning(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day1.start"));

        // Include master bedroom as option
        String room = getPlayerChoice(VerbParser.getMessage("day1.room.prompt"),
                new String[]{"kitchen", "living", "dining", "laundry", "foyer", "bath", "balcony", "master"});

        switch (room) {
            case "kitchen" -> kitchenCleaning(state);
            case "living" -> livingRoomCleaning(state);
            case "dining" -> diningRoomCleaning(state);
            case "laundry" -> laundryRoomCleaning(state);
            case "foyer" -> foyerCleaning(state);
            case "bath" -> bathroomCleaning(state);
            case "balcony" -> balconyCleaning(state);
            case "master" -> {
                if (state.hasKey) {
                    System.out.println(VerbParser.getMessage("day1.master.access"));
                    masterBedroomEntry(scanner, state);
                } else {
                    System.out.println(VerbParser.getMessage("master.blocked.door"));
                    System.out.println(VerbParser.getMessage("master.blocked.voice.gentle"));
                    System.out.println(VerbParser.getMessage("master.blocked.ko.sorry"));
                    caretakerReset(state);
                    return;
                }
                return;
            }
            default -> System.out.println(VerbParser.getMessage("day1.invalid.room"));
        }
    }


    /**
     * Day 2: Cooking Lesson
     */
    private static void day2CookingLesson(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day2.intro"));
        System.out.println(VerbParser.getMessage("day2.lesson"));
        System.out.println(VerbParser.getMessage("day2.demo"));

        // First choice: attempt or watch
        String[] firstOptions = {"try cutting", "watch more"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day2.first.prompt"), firstOptions);

        if (firstChoice.equals("try cutting")) {
            // With Caretaker path
            System.out.println(VerbParser.getMessage("day2.try.take"));
            System.out.println(VerbParser.getMessage("day2.try.guide"));
            state.withCaretakerScore++;

            // Second choice after guidance
            String[] secondOptions = {"chop vegetables", "say no thanks"};
            String secondChoice = getPlayerChoice(VerbParser.getMessage("day2.try.second.prompt"), secondOptions);

            if (secondChoice.equals("chop vegetables")) {
                System.out.println(VerbParser.getMessage("day2.try.chop"));
                System.out.println(VerbParser.getMessage("day2.try.good"));
                state.withCaretakerScore += 2;
            } else {
                System.out.println(VerbParser.getMessage("day2.try.no"));
                System.out.println(VerbParser.getMessage("day2.try.fine"));
                state.againstCaretakerScore++;
            }
        } else {
            // Neutral path
            System.out.println(VerbParser.getMessage("day2.watch.ask"));
            System.out.println(VerbParser.getMessage("day2.watch.demo"));

            // Follow-up choice
            String[] followOptions = {"try now", "no thanks"};
            String followChoice = getPlayerChoice(VerbParser.getMessage("day2.watch.follow.prompt"), followOptions);

            if (followChoice.equals("try now")) {
                System.out.println(VerbParser.getMessage("day2.watch.try"));
                System.out.println(VerbParser.getMessage("day2.watch.see"));
                state.withCaretakerScore++;
            } else {
                System.out.println(VerbParser.getMessage("day2.watch.no"));
                System.out.println(VerbParser.getMessage("day2.watch.suit"));
            }
        }

        System.out.println(VerbParser.getMessage("day2.end.sizzle"));
        System.out.println(VerbParser.getMessage("day2.end.lunch"));
    }


    /**
     * Day 3: Knife Training - for defending against "animals" with the caretaker.
     */
    private static void day3KnifeTraining(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day3.intro"));
        System.out.println(VerbParser.getMessage("day3.explain"));
        System.out.println(VerbParser.getMessage("day3.demo"));

        String[] firstOptions = {"try grip", "sounds scary"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day3.first.prompt"), firstOptions);

        if (firstChoice.equals("try grip")) {
            System.out.println(VerbParser.getMessage("day3.try.firm"));
            System.out.println(VerbParser.getMessage("day3.try.stance"));
            state.withCaretakerScore++;

            String[] secondOptions = {"thrust throat", "too high"};
            String secondChoice = getPlayerChoice(VerbParser.getMessage("day3.try.second.prompt"), secondOptions);

            if (secondChoice.equals("thrust throat")) {
                System.out.println(VerbParser.getMessage("day3.try.thrust"));
                System.out.println(VerbParser.getMessage("day3.try.perfect"));
                state.withCaretakerScore += 2;
            } else {
                System.out.println(VerbParser.getMessage("day3.try.too.high"));
                System.out.println(VerbParser.getMessage("day3.try.low"));
            }
        } else {
            System.out.println(VerbParser.getMessage("day3.scary.response"));
            System.out.println(VerbParser.getMessage("day3.scary.comfort"));
            System.out.println(VerbParser.getMessage("day3.scary.demo"));

            String[] followOptions = {"try now", "maybe later"};
            String followChoice = getPlayerChoice(VerbParser.getMessage("day3.scary.follow.prompt"), followOptions);

            if (followChoice.equals("try now")) {
                System.out.println(VerbParser.getMessage("day3.scary.try"));
                state.withCaretakerScore++;
            }
        }

        System.out.println(VerbParser.getMessage("day3.end.distance"));
        System.out.println(VerbParser.getMessage("day3.end.need"));
    }


    /**
     * Day 4: Paintball Practice
     */
    private static void day4PaintballPractice(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day4.intro"));
        System.out.println(VerbParser.getMessage("day4.range"));
        System.out.println(VerbParser.getMessage("day4.demo"));

        String[] firstOptions = {"ready to shoot", "check safety"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day4.first.prompt"), firstOptions);

        if (firstChoice.equals("ready to shoot")) {
            System.out.println(VerbParser.getMessage("day4.shoot.finger"));
            System.out.println(VerbParser.getMessage("day4.shoot.kick"));
            state.withCaretakerScore++;

            String[] secondOptions = {"aim center", "shoot again"};
            String secondChoice = getPlayerChoice(VerbParser.getMessage("day4.second.prompt"), secondOptions);

            if (secondChoice.equals("aim center")) {
                System.out.println(VerbParser.getMessage("day4.center.sights"));
                System.out.println(VerbParser.getMessage("day4.center.perfect"));
                state.withCaretakerScore += 2;
            } else {
                System.out.println(VerbParser.getMessage("day4.again.crack"));
                System.out.println(VerbParser.getMessage("day4.again.close"));
            }
        } else {
            System.out.println(VerbParser.getMessage("day4.check.response"));
            System.out.println(VerbParser.getMessage("day4.check.smart"));

            String[] followOptions = {"shoot now", "one more check"};
            String followChoice = getPlayerChoice(VerbParser.getMessage("day4.follow.prompt"), followOptions);

            if (followChoice.equals("shoot now")) {
                System.out.println(VerbParser.getMessage("day4.follow.shoot"));
                state.withCaretakerScore++;
            } else {
                System.out.println(VerbParser.getMessage("day4.follow.check"));
            }
        }

        System.out.println(VerbParser.getMessage("day4.end.loud"));
        System.out.println(VerbParser.getMessage("day4.end.smell"));
    }

    /**
     * Day 5: Laundry & Stories
     */
    private static void day5LaundryStories(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day5.intro"));
        System.out.println(VerbParser.getMessage("day5.work"));

        String[] firstOptions = {"tell her", "fold quietly"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day5.first.prompt"), firstOptions);

        if (firstChoice.equals("tell her")) {
            System.out.println(VerbParser.getMessage("day5.tell.response"));
            System.out.println(VerbParser.getMessage("day5.tell.memory"));
            System.out.println(VerbParser.getMessage("day5.tell.laugh"));
            state.withCaretakerScore++;

            String[] memoryOptions = {"try to remember", "ask about birthday"};
            String memoryChoice = getPlayerChoice(VerbParser.getMessage("day5.memory.prompt"), memoryOptions);

            if (memoryChoice.equals("try to remember")) {
                System.out.println(VerbParser.getMessage("day5.memory.try"));
                System.out.println(VerbParser.getMessage("day5.memory.baker"));
                state.withCaretakerScore += 2;
            } else {
                System.out.println(VerbParser.getMessage("day5.memory.ask"));
                System.out.println(VerbParser.getMessage("day5.memory.picnic"));
                System.out.println(VerbParser.getMessage("day5.memory.smile"));
                state.againstCaretakerScore++;
            }
        } else {
            System.out.println(VerbParser.getMessage("day5.quiet.response"));
            System.out.println(VerbParser.getMessage("day5.quiet.basket"));
            System.out.println(VerbParser.getMessage("day5.quiet.carried"));

            String[] reactOptions = {"sweet memory", "sounds nice"};
            String reactChoice = getPlayerChoice(VerbParser.getMessage("day5.react.prompt"), reactOptions);

            if (reactChoice.equals("sweet memory")) {
                System.out.println(VerbParser.getMessage("day5.react.sweet"));
                System.out.println(VerbParser.getMessage("day5.react.beams"));
                state.withCaretakerScore++;
            } else {
                System.out.println(VerbParser.getMessage("day5.react.nice"));
                System.out.println(VerbParser.getMessage("day5.react.soft"));
            }
        }

        System.out.println(VerbParser.getMessage("day5.end.sock"));
        System.out.println(VerbParser.getMessage("day5.end.matter"));
    }


    /**
     * Day 6: Balcony Time
     * Tests Ko's contentment vs curiosity.
     */
    private static void day6BalconyReflection(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day6.intro"));
        System.out.println(VerbParser.getMessage("day6.breeze"));

        if (state.hasKey) {
            System.out.println(VerbParser.getMessage("day6.key"));
        }

        if (state.foundStudy && state.hasGem) {
            goodBalconyEnding(scanner, state);
            return;
        }

        String[] firstOptions = {"clean railing", "look outside"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day6.first.prompt"), firstOptions);

        if (firstChoice.equals("clean railing")) {
            System.out.println(VerbParser.getMessage("day6.clean.rust"));
            System.out.println(VerbParser.getMessage("day6.clean.voice"));
            state.withCaretakerScore++;

            String[] secondOptions = {"finish quick", "enjoy breeze"};
            String secondChoice = getPlayerChoice(VerbParser.getMessage("day6.clean.second.prompt"), secondOptions);

            if (secondChoice.equals("finish quick")) {
                System.out.println(VerbParser.getMessage("day6.finish.shines"));
                state.withCaretakerScore++;
            } else {
                System.out.println(VerbParser.getMessage("day6.enjoy.breeze"));
            }
        } else {
            System.out.println(VerbParser.getMessage("day6.look.world"));
            System.out.println(VerbParser.getMessage("day6.look.life"));

            String[] followOptions = {"keep looking", "back to work"};
            String followChoice = getPlayerChoice(VerbParser.getMessage("day6.look.follow.prompt"), followOptions);

            if (followChoice.equals("keep looking")) {
                System.out.println(VerbParser.getMessage("day6.keep.looking"));
                state.againstCaretakerScore++;
            } else {
                System.out.println(VerbParser.getMessage("day6.back.work"));
            }
        }

        System.out.println(VerbParser.getMessage("day6.end.time"));
        System.out.println(VerbParser.getMessage("day6.end.breeze"));
    }

    /**
     * Day 7: Guest Prep
     */
    private static void day7GuestPrep(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day7.intro"));
        System.out.println(VerbParser.getMessage("day7.explain"));
        System.out.println(VerbParser.getMessage("day7.eyes"));

        String[] firstOptions = {"kitchen first", "dining first", "ask guest"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day7.first.prompt"), firstOptions);

        if (firstChoice.equals("kitchen first")) {
            System.out.println(VerbParser.getMessage("day7.kitchen.polish"));
            System.out.println(VerbParser.getMessage("day7.kitchen.approve"));
            state.withCaretakerScore++;

            String[] secondOptions = {"dining now", "ask about guest"};
            String secondChoice = getPlayerChoice(VerbParser.getMessage("day7.kitchen.second.prompt"), secondOptions);

            if (secondChoice.equals("dining now")) {
                System.out.println(VerbParser.getMessage("day7.kitchen.dining"));
                System.out.println(VerbParser.getMessage("day7.kitchen.presentation"));
                state.withCaretakerScore += 2;
            } else {
                System.out.println(VerbParser.getMessage("day7.kitchen.ask"));
                System.out.println(VerbParser.getMessage("day7.kitchen.friend"));
            }
        } else if (firstChoice.equals("dining first")) {
            System.out.println(VerbParser.getMessage("day7.dining.first"));
            System.out.println(VerbParser.getMessage("day7.dining.kitchen"));
            state.withCaretakerScore++;
        } else {
            System.out.println(VerbParser.getMessage("day7.ask.guest"));
            System.out.println(VerbParser.getMessage("day7.ask.visitor"));
            System.out.println(VerbParser.getMessage("day7.ask.kitchen"));
            state.againstCaretakerScore++;
        }

        System.out.println(VerbParser.getMessage("day7.end.tomorrow"));
        System.out.println(VerbParser.getMessage("day7.end.excitement"));
    }

    /**
     * Day 8: Bad Ending
     */
    private static void day8BadEnding(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("day8.intro"));
        System.out.println(VerbParser.getMessage("day8.ready"));

        String[] firstOptions = {"make dinner", "who is guest"};
        String firstChoice = getPlayerChoice(VerbParser.getMessage("day8.first.prompt"), firstOptions);

        if (firstChoice.equals("make dinner")) {
            System.out.println(VerbParser.getMessage("day8.dinner.choice"));
        } else {
            System.out.println(VerbParser.getMessage("day8.who.choice"));
            System.out.println(VerbParser.getMessage("day8.partner"));
            System.out.println(VerbParser.getMessage("day8.enthusiasm"));
        }

        System.out.println(VerbParser.getMessage("day8.chopping"));
        System.out.println(VerbParser.getMessage("day8.door"));
        System.out.println(VerbParser.getMessage("day8.delivery"));

        System.out.println(VerbParser.getMessage("day8.dinner.served"));
        System.out.println(VerbParser.getMessage("day8.rest"));

        if (state.hasGameBoy) {
            System.out.println(VerbParser.getMessage("day8.living.gameboy"));
        } else {
            System.out.println(VerbParser.getMessage("day8.living.wait"));
        }

        System.out.println(VerbParser.getMessage("day8.caretaker.enters"));
        System.out.println(VerbParser.getMessage("day8.device"));

        String[] deviceOptions = {"take it", "what is it"};
        String deviceChoice = getPlayerChoice(VerbParser.getMessage("day8.device.prompt"), deviceOptions);

        if (deviceChoice.equals("take it")) {
            System.out.println(VerbParser.getMessage("day8.take.device"));
        } else {
            System.out.println(VerbParser.getMessage("day8.what.device"));
            System.out.println(VerbParser.getMessage("day8.surprise"));
            System.out.println(VerbParser.getMessage("day8.smile"));
        }

        System.out.println(VerbParser.getMessage("day8.balcony"));
        System.out.println(VerbParser.getMessage("day8.talk"));
        System.out.println(VerbParser.getMessage("day8.radio"));

        System.out.println(VerbParser.getMessage("day8.press"));
        System.out.println(VerbParser.getMessage("day8.boom"));
        System.out.println(VerbParser.getMessage("day8.smile.spread"));

        System.out.println(VerbParser.getMessage("day8.beautiful"));
        System.out.println(VerbParser.getMessage("day8.yeah"));

        state.badEndingReached = true;
    }

    public static void kitchenCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.kitchen.desc"));

        String[] tasks = {"counters", "stove", "sink", "floor", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.kitchen.prompt"), tasks);

        switch (choice) {
            case "counters" -> {
                System.out.println(VerbParser.getMessage("clean.kitchen.counters"));
            }
            case "stove" -> {
                System.out.println(VerbParser.getMessage("clean.kitchen.stove"));
            }
            case "sink" -> {
                System.out.println(VerbParser.getMessage("clean.kitchen.sink"));
                state.withCaretakerScore++;
            }
            case "floor" -> {
                System.out.println(VerbParser.getMessage("clean.kitchen.floor"));
            }
            default -> {}
        }

        if (rand.nextBoolean()) {
            System.out.println(VerbParser.getMessage("clean.kitchen.snack"));
            if (getPlayerChoice(VerbParser.getMessage("clean.kitchen.snack.prompt"), new String[]{"yes", "no"}).equals("yes")) {
                System.out.println(VerbParser.getMessage("clean.kitchen.snack.yes"));
            }
        }
        state.withCaretakerScore++;
    }

    public static void livingRoomCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.living.desc"));

        String[] tasks = {"shelves", "furniture", "windows", "plant", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.living.prompt"), tasks);

        if (choice.equals("shelves")) {
            int resets = ResetCounter.getInstance().getResetCount();
            boolean clumsy = (resets > 0 && state.shelfCleanedResets > 0) || rand.nextInt(2) < 1;

            if (clumsy) {
                System.out.println(VerbParser.getMessage("clean.living.shelves.clumsy"));
                String keep = getPlayerChoice(VerbParser.getMessage("clean.living.key.prompt"), new String[]{"keep", "return"});
                if (keep.equals("keep")) {
                    state.hasKey = true;
                    state.wasResetThisTurn = false;  // ENSURE flag is cleared
                    System.out.println(VerbParser.getMessage("clean.living.key.keep"));
                    main.saveGameState(state);
                } else {
                    System.out.println(VerbParser.getMessage("clean.living.key.return"));
                }
            } else {
                System.out.println(VerbParser.getMessage("clean.living.shelves.clean"));
                state.shelfCleanedResets++;
            }
        } else if (choice.equals("windows")) {
            System.out.println(VerbParser.getMessage("clean.living.windows"));
        } else if (choice.equals("furniture")) {
            System.out.println(VerbParser.getMessage("clean.living.furniture"));
        }
    }

    public static void diningRoomCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.dining.desc"));

        String[] tasks = {"table", "chairs", "chandelier", "silver", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.dining.prompt"), tasks);

        switch (choice) {
            case "table" -> {
                System.out.println(VerbParser.getMessage("clean.dining.table"));
                state.withCaretakerScore++;
            }
            case "chandelier" -> {
                System.out.println(VerbParser.getMessage("clean.dining.chandelier"));
            }
            case "silver" -> {
                System.out.println(VerbParser.getMessage("clean.dining.silver"));
            }
        }
    }

    public static void laundryRoomCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.laundry.desc"));

        String[] tasks = {"washer", "dryer", "folding", "sorting", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.laundry.prompt"), tasks);

        switch (choice) {
            case "sorting" -> {
                System.out.println(VerbParser.getMessage("clean.laundry.sorting"));
                state.againstCaretakerScore++;
            }
            case "folding" -> {
                System.out.println(VerbParser.getMessage("clean.laundry.folding"));
            }
        }
    }

    public static void foyerCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.foyer.desc"));

        String[] tasks = {"floor", "coats", "door", "mail", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.foyer.prompt"), tasks);

        if (choice.equals("door")) {
            System.out.println(VerbParser.getMessage("clean.foyer.door"));
            state.againstCaretakerScore++;
        }
    }

    public static void bathroomCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.bath.desc"));

        String[] tasks = {"sink", "tub", "mirror", "tiles", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.bath.prompt"), tasks);

        if (choice.equals("mirror")) {
            System.out.println(VerbParser.getMessage("clean.bath.mirror"));
            state.againstCaretakerScore++;
        }
    }

    public static void balconyCleaning(main.GameState state) {
        System.out.println(VerbParser.getMessage("clean.balcony.desc"));

        String[] tasks = {"railing", "chairs", "plants", "stare", "done"};
        String choice = getPlayerChoice(VerbParser.getMessage("clean.balcony.prompt"), tasks);

        if (choice.equals("stare")) {
            System.out.println(VerbParser.getMessage("clean.balcony.stare"));
            state.againstCaretakerScore++;
        }
    }

    /**
     * Caretaker reset - ends current day and increases reset counter.
     */
    public static void caretakerReset(main.GameState state) {
        int resets = ResetCounter.getInstance().getResetCount();
        String resetFlavorKey = switch (resets) {
            case 0 -> "reset.flavor.0";
            case 1, 2 -> "reset.flavor.12";
            default -> "reset.flavor.default";
        };

        System.out.println("\n" + VerbParser.getMessage(resetFlavorKey));
        ResetCounter.getInstance().incrementReset();

        state.currentDay = 0;
        state.wasResetThisTurn = true;
        main.saveGameState(state);

        main.logger.warn("Reset triggered. Day reset.");
    }

    /**
     * Master Bedroom -> Study discovery.
     */
    private static void masterBedroomEntry(Scanner scanner, main.GameState state) {
        System.out.println("\n" + VerbParser.getMessage("master.entry.key_turns"));
        System.out.println(VerbParser.getMessage("master.entry.pristine"));

        String[] exploreOptions = {"check wall", "look around", "leave"};
        String exploreChoice = getPlayerChoice(VerbParser.getMessage("master.entry.prompt"), exploreOptions);

        if (exploreChoice.equals("check wall")) {
            System.out.println("\n" + VerbParser.getMessage("master.entry.panel"));
            System.out.println(VerbParser.getMessage("master.entry.study"));
            state.foundStudy = true;
            studyDiscovery(scanner, state);
        }
    }

    /**
     * Study: Caretaker's experiment notes.
     */
    private static void studyDiscovery(Scanner scanner, main.GameState state) {
        System.out.println("\n" + VerbParser.getMessage("study.discovery.papers"));
        System.out.println(VerbParser.getMessage("study.discovery.subject"));

        String[] readOptions = {"read more", "take notes", "leave"};
        String readChoice = getPlayerChoice(VerbParser.getMessage("study.discovery.prompt"), readOptions);

        if (readChoice.equals("read more")) {
            System.out.println("\n" + VerbParser.getMessage("study.discovery.good_nature"));
            System.out.println(VerbParser.getMessage("study.discovery.jolt"));
            state.hasGem = true;
            ResetCounter.getInstance().incrementReset();
            System.out.println(VerbParser.getMessage("study.discovery.wake"));
            main.saveGameState(state);
        }
    }

    /**
     * Good Ending: Balcony confrontation and escape.
     */
    private static void goodBalconyEnding(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("good.balcony.radio_quiet"));
        System.out.println(VerbParser.getMessage("good.balcony.caretaker_leaves"));

        String[] balconyOptions = {"wait patiently", "talk male"};
        String balconyChoice = getPlayerChoice(VerbParser.getMessage("good.balcony.prompt"), balconyOptions);

        System.out.println(VerbParser.getMessage("good.balcony.radio_shoot"));

        if (balconyChoice.equals("wait patiently")) {
            System.out.println(VerbParser.getMessage("good.balcony.gun_ready"));
            System.out.println(VerbParser.getMessage("good.balcony.paintball_hurt"));
            state.withCaretakerScore++; // Obeying
        }

        String[] shootOptions = {"pull trigger", "can't shoot"};
        String shootChoice = getPlayerChoice(VerbParser.getMessage("good.balcony.shoot_prompt"), shootOptions);

        if (shootChoice.equals("pull trigger")) {
            System.out.println(VerbParser.getMessage("good.balcony.crack_paint"));
            state.badEndingReached = true;
        } else {
            System.out.println(VerbParser.getMessage("good.balcony.cant_shoot"));
            System.out.println(VerbParser.getMessage("good.balcony.scream_radio"));

            System.out.println(VerbParser.getMessage("good.balcony.male_hand"));
            System.out.println(VerbParser.getMessage("good.balcony.poor_thing"));
            System.out.println(VerbParser.getMessage("good.balcony.gem"));

            System.out.println(VerbParser.getMessage("good.balcony.tinkering"));
            System.out.println(VerbParser.getMessage("good.balcony.jolt"));

            System.out.println(VerbParser.getMessage("good.balcony.she_ready"));
            System.out.println(VerbParser.getMessage("good.balcony.why_not"));
            System.out.println(VerbParser.getMessage("good.balcony.changes_anything"));

            goodEscapeWakeup(scanner, state);
        }
    }

    /**
     * Wake up remembering. Subtle escape plan.
     */
    private static void goodEscapeWakeup(Scanner scanner, main.GameState state) {
        System.out.println(VerbParser.getMessage("good.escape.wake_normal"));
        System.out.println(VerbParser.getMessage("good.escape.caretaker_enters"));

        String[] normalOptions = {"yes clean", "feel strange"};
        String normalChoice = getPlayerChoice(VerbParser.getMessage("good.escape.good_prompt"), normalOptions);

        System.out.println(VerbParser.getMessage("good.escape.leaves_bedroom"));

        System.out.println(VerbParser.getMessage("good.escape.foyer_door"));
        System.out.println(VerbParser.getMessage("good.escape.male_whispers"));

        if (normalChoice.equals("yes clean") || state.foundStudy) {
            System.out.println(VerbParser.getMessage("good.escape.smiles_unlocks"));
            System.out.println(VerbParser.getMessage("good.escape.go_stall"));
            System.out.println(VerbParser.getMessage("good.escape.door_opens"));

            System.out.println(VerbParser.getMessage("good.escape.good_ending"));
            System.out.println(VerbParser.getMessage("good.escape.resets_endured") + ResetCounter.getInstance().getResetCount());
            System.out.println(VerbParser.getMessage("good.escape.ko_escapes"));
            state.badEndingReached = true; // Ends game
        }
    }

    // Utilities
    private static String getPlayerChoice(String prompt, String[] valid) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt + " ");
            String choice = scanner.nextLine().trim().toLowerCase();
            for (String v : valid) {
                if (choice.startsWith(v.substring(0, Math.min(3, v.length())))) {
                    return v;
                }
            }
            System.out.println(VerbParser.getMessage("choice.try_again"));
        }
    }

    /**
     * Time period generator
     */
    private static String getRandomTimePeriod(int dayNum, int resets) {
        String[] sequentialDays = {
                "Monday morning",    // Day 0
                "Tuesday dawn",      // Day 1
                "Wednesday haze",    // Day 2
                "Thursday chill",    // Day 3
                "Friday dusk",       // Day 4
                "Saturday mist",     // Day 5
                "Sunday quiet",      // Day 6
                "Final Sunday"       // Day 7
        };

        String basePeriod = sequentialDays[dayNum];
        String loopModifier = switch (resets) {
            case 0 -> "";
            case 1, 2 -> " (echoes familiar)";
            case 3, 4 -> " (rehearsed rhythm)";
            default -> " (endless repetition)";
        };

        return basePeriod + loopModifier;
    }
}

