package org.example;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) throws Exception {
        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("Bot token is missing! Set the BOT_TOKEN environment variable.");
        }
        JDABuilder.createDefault(token)
                .addEventListeners(new DiscordBot())
                .build();
    }

    private String matchCreatorId;
    String description;
    String description1;
    ArrayList<String> player = new ArrayList<>();
    ArrayList<String> player1 = new ArrayList<>();
    String risingtides = "https://wiki.conflictnations.com/images/thumb/c/c1/RisingTides_Frame_v2_%281%29.gif/380px-RisingTides_Frame_v2_%281%29.gif";
    String ww3 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9LAA9pTsrubSazo9vOP8cGzv7sMdx2WYSKA&s";
    String overkill = "https://wiki.conflictnations.com/images/thumb/f/fa/2020-05-30_Overkill.png/380px-2020-05-30_Overkill.png";
    String con = "https://i.ytimg.com/vi/zewB_SSL9WA/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLBJRlOBTBD-fHhN3s0fDQktGLRkyA";
    String flashpoint = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/clans/31932263/83472e81d1f50bb516d7df4c41ce37cab04bb34b.png";
    String photo;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!ava")) {

            resetState();

            event.getChannel().sendMessage("enter game description: match type and start date").queue();



            waitForNextMessage(event).thenAccept(response -> {
                description = response;
//                event.getChannel().sendMessage("You said: " + response).queue();
//                event.getChannel().sendMessage("Now I can continue the code!").queue();

                MessageEmbed embed = createEmbed();
                event.getChannel().sendMessageEmbeds(embed).setActionRow(
                        Button.success("sign-up-button", "sign up"),
                        Button.danger("turn-down-button", "forfeit"),
                        Button.primary("consider-button", "considering")
                ).queue(message -> embedMessageId = message.getId());




                String username = event.getMember().getEffectiveName();
                //event.getChannel().sendMessage("Pong! " + username + " " + description).queue();
            });
        }
        else if (event.getMessage().getContentRaw().equalsIgnoreCase("!pub")) {



            matchCreatorId = event.getAuthor().getId();
            event.getChannel().sendMessage("enter game description: match type and start date").queue();



            waitForNextMessage(event).thenAccept(response -> {
                description1 = response;
//                event.getChannel().sendMessage("You said: " + response).queue();
//                event.getChannel().sendMessage("Now I can continue the code!").queue();

                MessageEmbed embed = createNewGameEmbed();
                event.getChannel().sendMessageEmbeds(embed).setActionRow(
                        Button.success("sign-up-button1", "sign up"),
                        Button.danger("turn-down-button1", "forfeit"),
                        Button.primary("consider-button1", "considering")
                ).queue(message -> embedMessageId1 = message.getId());




                String username = event.getMember().getEffectiveName();
                resetState2();
            });
        }
    }

    private String embedMessageId;
    private String embedMessageId1;
    String clist = "";
    String clist1 = "-----------\n";
    String map = "";
    @Override
    public void onButtonInteraction(@NotNull net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent event) {
        String userId = event.getUser().getId();
        String username = event.getMember().getEffectiveName();

        if (event.getComponentId().equals("sign-up-button")) {
            //System.out.println(username + " clicked Sign Up!"); // Debugging log

            if (!player.contains(username)) {
                player.add(username);
            }

            event.getChannel().editMessageEmbedsById(embedMessageId, createUpdatedEmbed()).queue();

            event.reply(username + ", pick a role:")
                    .addActionRow(
                            Button.primary("(ground)", "Ground"),
                            Button.primary("(helis)", "Air (Helis)"),
                            Button.primary("(sasf)", "Air (SASF)"),
                            Button.primary("(feeder)", "Feeder")
                    )
                    .setEphemeral(true) // Only the user can see this
                    .queue(/*response -> System.out.println("Role selection sent!"), error -> error.printStackTrace()*/);
        }

        else if(event.getComponentId().equals("consider-button")) {
            String username1 = event.getMember().getEffectiveName();
            clist += username1 + "\n";
            event.getChannel().editMessageEmbedsById(embedMessageId, createUpdatedEmbed()).queue();
            //event.deferReply(true).queue();

        }

        else if(event.getComponentId().equals("turn-down-button")) {
            String username2 = event.getMember().getEffectiveName();

//            if (username2.endsWith(")")) {
//                player.remove(username2);
//            }

            player.removeIf(p -> p.startsWith(username2));
            event.getChannel().editMessageEmbedsById(embedMessageId, createUpdatedEmbed()).queue();
            //event.deferReply(true).queue();
        }

            if(event.getComponentId().endsWith(")")) {
                String role = event.getComponentId();


                int index = player.indexOf(username);
                if (index != -1) {
                    player.set(index, username + " - " + role);
                }

                // Correctly update the embed message using its stored ID
                event.getChannel().editMessageEmbedsById(embedMessageId, createUpdatedEmbed()).queue();
                //event.reply("You selected: " + role).setEphemeral(true).queue();
            }

        //String userId = event.getUser().getId();
        String username1 = event.getMember().getEffectiveName();

        if (event.getComponentId().equals("sign-up-button1")) {
            //System.out.println(username1 + " clicked Sign Up!"); // Debugging log

            if (!player1.contains(username1)) {
                player1.add(username1);
            }
            if (embedMessageId1 == null) {
                System.out.println("embedMessageId1 is null, cannot update embed!");
            } else {
                System.out.println("Editing embed with ID: " + embedMessageId1);
            }

            System.out.println("Added player: " + username1);

            event.getChannel().editMessageEmbedsById(embedMessageId1, createUpdatedGameEmbed()).queue();
            System.out.println(username1 + " clicked Sign Up again!"); // Debugging log

            String userIdx = event.getUser().getId();


            if (!userIdx.equals(matchCreatorId)) {
                return;
            }

            event.deferReply(true).queue(hook -> {
                hook.sendMessage("Pick a map:")
                        .addActionRow(
                                Button.primary("ww3_4x", "WW3 4x"),
                                Button.primary("ww3_1x", "WW3 1x"),
                                Button.primary("ww3_apocalypse_4x", "WW3 Apocalypse 4x"),
                                Button.primary("rising_tides_1x", "Rising Tides 1x"),
                                Button.primary("flashpoint_1x", "Flashpoint 1x")
                        )
                        .addActionRow(
                                Button.primary("overkill_1x", "overkill 1x"),
                                Button.primary("overkill_4x", "Overkill 4x")
                                )
                        .queue();
            });
            //System.out.println(username1 + " clicked Sign Up again again!"); // Debugging log

        }

        else if(event.getComponentId().equals("consider-button1")) {
            String usernamex = event.getMember().getEffectiveName();
            clist1 += usernamex + "\n" + "------" + "\n";
            event.getChannel().editMessageEmbedsById(embedMessageId1, createUpdatedGameEmbed()).queue();
            //event.deferReply(true).queue();

        }

        else if(event.getComponentId().equals("turn-down-button1")) {
            String username2 = event.getMember().getEffectiveName();

//            if (username2.endsWith(")")) {
//                player.remove(username2);
//            }

            player1.removeIf(p -> p.startsWith(username2));
            event.getChannel().editMessageEmbedsById(embedMessageId1, createUpdatedGameEmbed()).queue();
            //event.deferReply(true).queue();
        }

        if(event.getComponentId().endsWith("x")) {

            String userIdx = event.getUser().getId();


            if (!userIdx.equals(matchCreatorId)) {
                return;
            }

            map = event.getComponentId();

            if(event.getComponentId().equals("ww3_1x") || event.getComponentId().equals("ww3_4x")) {
                photo = ww3;
            }

            else if(event.getComponentId().equals("overkill_1x") || event.getComponentId().equals("overkill_4x")) {
                photo = overkill;
            }

            else if(event.getComponentId().equals("rising_tides_1x")) {
                photo = risingtides;
            }

            else if(event.getComponentId().equals("flashpoint_1x")) {
                photo = flashpoint;
            }


//            int index = player1.indexOf(username);
//            if (index != -1) {
//                player1.set(index, username + " - " + role);
//            }

            // Correctly update the embed message using its stored ID
            event.getChannel().editMessageEmbedsById(embedMessageId1, createUpdatedGameEmbed()).queue();
            //event.reply("You selected: " + role).setEphemeral(true).queue();
        }

    }






    private CompletableFuture<String> waitForNextMessage(MessageReceivedEvent event) {
        CompletableFuture<String> future = new CompletableFuture<>();

        event.getJDA().addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent nextEvent) {
                if (nextEvent.getAuthor().equals(event.getAuthor()) &&
                        nextEvent.getChannel().equals(event.getChannel())) {
                    future.complete(nextEvent.getMessage().getContentRaw());
                    event.getJDA().removeEventListener(this); // Remove listener to prevent memory leaks
                }
            }
        });

        return future;
    }

    public MessageEmbed createEmbed() {
        return new EmbedBuilder()
                .setTitle("New AvA starting")
                .setDescription(description)
                .setColor(Color.orange)
                .setFooter("created on")
                .setImage("https://preview.redd.it/the-making-of-antarctica-scenario-v0-2bwo8l15satd1.jpg?width=730&format=pjpg&auto=webp&s=5d50d4cbc5b3573bdba4323adc6587a589912d5a")
                .addField("Player 1", "ground", true)
                .addField("Player 2", "ground", true)
                .addField("Player 3", "ground", true)
                .addField("Player 4", "air(helis)", true)
                .addField("Player 5", "air(sasf)", true)
                .addField("Player 6", "feeder", true)
                .addField("Player 7", "feeder", true)
                .setTimestamp(java.time.Instant.now())
                .build();
    }
    public MessageEmbed createUpdatedEmbed() {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("New AvA starting")
                .setDescription(description)
                .setColor(Color.ORANGE)
                .setFooter("created on")
                .setImage("https://preview.redd.it/the-making-of-antarctica-scenario-v0-2bwo8l15satd1.jpg?width=730&format=pjpg&auto=webp&s=5d50d4cbc5b3573bdba4323adc6587a589912d5a")
                .setTimestamp(java.time.Instant.now())
                .addField("Considering:", clist, false);

        // Add players dynamically
        for (int i = 0; i < player.size(); i++) {
            embed.addField(player.get(i), "", true);
        }

        return embed.build();
    }


      public MessageEmbed createNewGameEmbed(){
          EmbedBuilder embed = new EmbedBuilder()
                  .setTitle("New pub match starting")
                  .setDescription(description1)
                  .setColor(Color.ORANGE)
                  .setFooter("created on")
                  .setImage(con)
                  .setTimestamp(java.time.Instant.now())
                  .addField("Player 1", "", true)
                  .addField("Player 2", "", true)
                  .addField("Player 3", "", true)
                  .addField("Player 4", "", true)
                  .addField("Player 5", "", true);
        return embed.build();
      }

    public MessageEmbed createUpdatedGameEmbed() {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("New pub match starting: " + map)
                .setDescription(description1)
                .setColor(Color.ORANGE)
                .setFooter("created on")
                .setImage(photo)
                .setTimestamp(java.time.Instant.now())
                .addField("Considering:", clist1, false);

        // Add players dynamically
        for (int i = 0; i < player1.size(); i++) {
            embed.addField(player1.get(i), "", true);
        }

        return embed.build();
    }

    private void resetState() {
        player.clear();      // Clears player list for !ava


        embedMessageId = null;   // Resets embed message ID for !ava


        clist = "";    // Clears consideration list for !ava




        description = ""; // Clears game description
    }

    private void resetState2() {
        player1.clear();     // Clears player list for !pub
        embedMessageId1 = null;  // Resets embed message ID for !pub
        clist1 = "-----------\n"; // Clears consideration list for !pub
        map = "";   // Clears map
        //photo = ""; induces exception
        description1 = ""; // Clears game description
    }


}

