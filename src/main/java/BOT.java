import database.DataBase;
import database.Table_Chat;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import weather.DailyWeatherResponse;
import weather.WeatherData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static hiddenData.HiddenData.*;

public class BOT extends TelegramLongPollingBot {
    private static final DataBase DB = new DataBase();
    private static final List<ScheduledFuture<?>> scheduledFutureList = new ArrayList<>();

    public BOT() {
        super();
        updateSubscribersThreads(DB.getSubscribersList());
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BOT());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String request = message.getText();
        Integer chatID = Integer.parseInt(message.getChatId().toString());
        if (message.getLocation() != null) {
            Float latitude = message.getLocation().getLatitude();
            Float longitude = message.getLocation().getLongitude();
            if (DB.existsChat(chatID)) {
                DB.updateLocation(chatID, latitude, longitude);
            } else {
                DB.addRow(chatID, latitude, longitude);
            }
            sendMessage(chatID.toString(), "Местоположение обновлено! Текущие координаты: " + latitude + ", " + longitude);
        } else if (request.equals("Посмотреть погоду на сегодня")) {
            sendTodayWeather(chatID);
        } else if (request.equals("Посмотреть погоду на завтра")) {
            sendTomorrowWeather(chatID);
        } else if (request.equals("Подписаться")) {
            if (!DB.isSubscribed(chatID)) {
                DB.subscribe(chatID);
                updateSubscribersThreads(DB.getSubscribersList());
                sendMessage(chatID.toString(), "Подписка оформлена");
            } else {
                sendMessage(chatID.toString(), "Подписка уже оформлена");
            }
        } else if (request.equals("Отписаться")) {
            if (DB.isSubscribed(chatID)) {
                DB.unsubscribe(chatID);
                updateSubscribersThreads(DB.getSubscribersList());
                sendMessage(chatID.toString(), "Подписка отменена");
            } else {
                sendMessage(chatID.toString(), "Подписка еще не оформлена");
            }
        } else {
            sendMessage(chatID.toString(), "Выберите опцию из предложенных");
        }
    }

    private synchronized void sendMessage(String chatID, String textMessage) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatID);
        message.setText(textMessage);
        boardAndButtons(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Message has not been sent!");
        }
    }

    private synchronized void boardAndButtons(SendMessage message) {
        ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();
        message.setReplyMarkup(board);
        board.setSelective(true);
        board.setResizeKeyboard(true);
        board.setOneTimeKeyboard(false);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow rowOne = new KeyboardRow();
        KeyboardRow rowTwo = new KeyboardRow();
        KeyboardRow rowThree = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestLocation(true);
        button.setText("Обновить местоположение");
        rowOne.add(button);
        rowTwo.add(new KeyboardButton("Посмотреть погоду на сегодня"));
        rowTwo.add(new KeyboardButton("Посмотреть погоду на завтра"));
        rowThree.add(new KeyboardButton("Подписаться"));
        rowThree.add(new KeyboardButton("Отписаться"));
        rowList.add(rowOne);
        rowList.add(rowTwo);
        rowList.add(rowThree);
        board.setKeyboard(rowList);
    }

    public String getFormattedWeatherResponse(DailyWeatherResponse weatherResponse, Integer dayNumber) {
        DecimalFormat unitedFormat = new DecimalFormat("#");
        DecimalFormat dividedFormat = new DecimalFormat("#.#");
        StringBuffer buffer = new StringBuffer();
        buffer.append("УТРО:").append("\n");
        buffer.append("   Температура: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getTemp().getMorn())).append("\n");
        buffer.append("   Ощущается как: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getFeels_like().getMorn())).append("\n");
        buffer.append("ДЕНЬ:").append("\n");
        buffer.append("   Температура: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getTemp().getDay())).append("\n");
        buffer.append("   Ощущается как: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getFeels_like().getDay())).append("\n");
        buffer.append("ВЕЧЕР:").append("\n");
        buffer.append("   Температура: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getTemp().getEve())).append("\n");
        buffer.append("   Ощущается как: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getFeels_like().getEve())).append("\n");
        buffer.append("НОЧЬ:").append("\n");
        buffer.append("   Температура: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getTemp().getNight())).append("\n");
        buffer.append("   Ощущается как: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getFeels_like().getNight())).append("\n");
        buffer.append("ВЛАЖНОСТЬ: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getHumidity())).append(" %\n");
        buffer.append("ВЕТЕР: ").append(dividedFormat.format(weatherResponse.getDaily(dayNumber).getWind_speed())).append(" м/с\n");
        buffer.append("ДАВЛЕНИЕ: ").append(unitedFormat.format(weatherResponse.getDaily(dayNumber).getPressure())).append(" мм рт. ст.\n");
        buffer.append("ТИП ПОГОДЫ: ").append(weatherResponse.getDaily(dayNumber).getWeather()[0].getDescription()).append("\n");
        return buffer.toString();
    }

    public void sendTodayWeather(Integer chatID) {
        DailyWeatherResponse weatherResponse = getWeather(chatID);
        sendMessage(chatID.toString(), getFormattedWeatherResponse(weatherResponse, 0));
    }

    public void sendTomorrowWeather(Integer chatID) {
        DailyWeatherResponse weatherResponse = getWeather(chatID);
        sendMessage(chatID.toString(), getFormattedWeatherResponse(weatherResponse, 1));
    }

    public DailyWeatherResponse getWeather(Integer chatID) {
        Table_Chat chatCoor = DB.getCertainChatRow(chatID);
        Float latitude = chatCoor.getLatitude();
        Float longitude = chatCoor.getLongitude();
        return WeatherData.getWeatherResponse(latitude, longitude);
    }

    private class NotifyingTask implements Runnable {
        Integer chatID;

        public NotifyingTask(Integer chatID) {
            this.chatID = chatID;
        }

        @Override
        public void run() {
            sendTodayWeather(chatID);
        }
    }

    public void updateSubscribersThreads(ArrayList<Integer> chats) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        for (ScheduledFuture<?> schedule : scheduledFutureList) {
            schedule.cancel(false);
        }
        for (Integer chat : chats) {
            scheduledFutureList.add(executor.scheduleAtFixedRate(new NotifyingTask(chat), 0, Integer.parseInt(NOTIFY_GAP_SECONDS.getRow()), TimeUnit.SECONDS));
        }
    }

    public String getBotUsername() {
        return BOT_NAME.getRow();
    }

    public String getBotToken() {
        return BOT_TOKEN.getRow();
    }
}
