package ru.pyzhov.VolgaIT_bot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Component
public class VolgaBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(VolgaBot.class);

    private static final String START = "/start";
    private static final String STAGES = "/stages";
    private static final String SOCIALS = "/socials";
    private static final String DISCIPLINES = "/disciplines";
    private static final String FAQ = "/faq";
    private static final String INFO = "/info";
    private static final String HELP = "/help";

    private void startCommand(Long chatId, String userName) {
        var text = """
            Добро пожаловать в бот, *%s*! 🎉
    
            Здесь вы сможете узнать актуальную информацию о олимпиаде *Волга IT*.
    
            Для этого воспользуйтесь командами:
            
            🔹 **/info** - общая информация о олимпиаде.  
            🔹 **/stages** - информация о этапах олимпиады и их сроках.  
            🔹 **/disciplines** - дисциплины олимпиады.  
            🔹 **/socials** - ссылки на социальные сети олимпиады.
            🔹 **/faq** - ответы на частозадаваемые вопросы.  
            🔹 **/ask** - задать нам вопрос.  
            
            Дополнительные команды:
            🔸 **/help** - получение справки по использованию бота.
            """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText, "Markdown");
    }

    public VolgaBot(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return "VolgaIT-bot";
    }

    private void sendMessage(Long chatId, String message, String parseMode) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatIdStr);
        sendMessage.setText(message);

        if (parseMode != null) {
            sendMessage.setParseMode(parseMode); // Устанавливаем разметку, если она указана
        }

        sendMessage.disableWebPagePreview();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка при отправке сообщения", e);
        }
    }

    private void unknownCommand(Long chatId) {
        var text = """
        ❓ Неизвестная команда.  
        Используйте /help, чтобы увидеть список доступных команд.
        """;
        sendMessage(chatId, text, "Markdown");
    }

    private void helpCommand(Long chatId) {
        var text = """
            *Справочная информация по боту* 📘

            Для получения информации об олимпиаде и взаимодействия с ботом, воспользуйтесь следующими командами:
            
            🔹 **/info** - общая информация о олимпиаде.  
            🔹 **/stages** - информация о этапах олимпиады и их сроках.  
            🔹 **/disciplines** - список дисциплин олимпиады.  
            🔹 **/socials** - ссылки на социальные сети олимпиады.
            🔹 **/faq** - ответы на частозадаваемые вопросы.  
            🔹 **/ask** - задать нам вопрос. 
            
            ❓ Если у вас возникли вопросы по работе с ботом или есть предложения, вы можете написать в Telegram: @nxf1ve.
            """;
        sendMessage(chatId, text, "Markdown");
    }

    private void faqCommand(Long chatId) {
        var text = """
            *Часто задаваемые вопросы (FAQ)* ❓

            🔹 *Кто может участвовать в олимпиаде?*  
            Олимпиада проводится среди граждан Российской Федерации и иностранных государств, родившихся в период с 2001 по 2008 год включительно.  

            🔹 *Когда олимпиада?*  
            Точные сроки следующей олимпиады ещё не определены, но стоит ориентироваться на осень 2025 года.  
            
            🔹 *Где можно найти задания, которые были на олимпиаде?*  
            Задания и решения прошлых лет можно найти на нашем [сайте](https://volga-it.org/past-tasks-and-solutions/). 

            🔹 *Какие дисциплины доступны?*  
            Ознакомьтесь с полным списком дисциплин с помощью команды /disciplines.  

            🔹 *Где узнать про этапы олимпиады?*  
            Информацию об этапах и сроках можно найти с помощью команды /stages.  

            Если ваш вопрос не указан здесь, вы можете задать его нам напрямую, воспользовавшись командой /ask.
            """;
        sendMessage(chatId, text, "Markdown");
    }

    private void handleAskCommand(Long chatId, String messageText) {
        String userQuestion = messageText.substring(4).trim();

        if (userQuestion.isEmpty()) {
            sendMessage(chatId, "❗️ Пожалуйста, напишите ваш вопрос после команды /ask.", "Markdown");
        } else {
            sendMessage(chatId, "✅ Спасибо за ваш вопрос! Мы рассмотрим его в ближайшее время.", "Markdown");

            sendToAdmin(chatId, userQuestion);
        }
    }

    private void sendToAdmin(Long userChatId, String question) {
        String adminChatId = "476779589";
        String message = String.format("📝 Новый вопрос от пользователя (Chat ID: %d):\n%s", userChatId, question);
        sendMessage(Long.valueOf(adminChatId), message, null);
    }

    private void stagesCommand(Long chatId) {
        var text = """
            *Этапы олимпиады Волга IT 2024* 🚀
            
            1️⃣ *Квалификация*  
            📅 _05.09.2024 - 28.10.2024_  
            Зарегистрировавшимся участникам нужно пройти тест в личном кабинете.  
            Можно выбрать несколько интересующих дисциплин.
            
            2️⃣ *Полуфинал*  
            📅 _13.09.2024 - 28.10.2024_  
            Этап проходит в *онлайн-формате*. Участники решают домашнее задание по выбранной дисциплине.
            
            3️⃣ *Финал*  
            📅 _25.11.2024 - 29.11.2024_  
            Этап проходит *очно* в городе *Ульяновск*. Участники решают задания по дисциплинам.
            
            📢 *Следите за обновлениями!*  
            Успехов всем участникам и до встречи на финале! ✨
            """;

        sendMessage(chatId, text, "Markdown");
    }

    private void infoCommand(Long chatId) {
        var text = """
            *Олимпиада Волга-IT* 🌍

            Волга-IT — это международная цифровая олимпиада для школьников и студентов в возрасте от 16 до 23 лет.  
            Наша уникальность — задания, основанные на реальных кейсах IT-компаний, с которыми им пришлось столкнуться.  
            Попробуй себя в решении рабочих задач и найди компанию мечты!

            📅 *Год основания*: 2006  
            👥 *Участников*: 24 000+  
            🌐 *Упоминаний в Интернете и СМИ*: 700 000+  
            🌍 *Государств-участников*: 27+  

            Прими участие в олимпиаде и открой новые горизонты для своей карьеры! 🚀
            
            🔧 *Хотите узнать больше?* 
            Напишите команду `/help`, чтобы получить информацию о работе бота.
            """;

        sendMessage(chatId, text, "Markdown");
    }

    private void socialsCommand(Long chatId) {
        var text = """
             *Добро пожаловать в сообщество Волга IT! 🚀*
            Подписывайтесь на наши социальные сети, чтобы не пропустить важные новости, анонсы и результаты олимпиады:
            
            🌐 *Сайт олимпиады*: [volga-it.org](http://volga-it.org/)                
            📱 *ВКонтакте*: [vk.com/volgait](https://vk.com/volgait)
            📺 *YouTube*: [youtube.com/@volgait](https://www.youtube.com/@volgait)
            💬 *Telegram*: [t.me/volgait](https://t.me/volgait)
            👥 Telegram-чат для общения: [t.me/volgaitx](https://t.me/volgaitx)
            
            📢 Будьте всегда в курсе!
            Мы публикуем важные объявления, задания прошлых лет, а также результаты олимпиады!
            """;

        sendMessage(chatId, text, "Markdown");
    }

    private void disciplinesCommand(Long chatId) {
        var text = """
            *Дисциплины олимпиады Волга IT 2024* 🎓
            
            📚 Участники могут попробовать свои силы в следующих направлениях:
            
            1️⃣ **1С программирование**  
            Углубленное изучение и решение задач на платформе 1С.  

            2️⃣ **Backend-разработка: Web API**  
            Создание и интеграция серверных приложений с использованием современных технологий.  

            3️⃣ **Web-дизайн**  
            Дизайн и прототипирование веб-интерфейсов, а также их визуальное оформление.  

            4️⃣ **Автоматизация тестирования (Java)**  
            Разработка автоматических тестов для обеспечения качества программного обеспечения.  

            5️⃣ **Искусственный интеллект и анализ данных**  
            Построение моделей машинного обучения и обработка больших данных.  

            6️⃣ **Мобильные приложения Flutter**  
            Разработка кроссплатформенных мобильных приложений на основе Flutter.  

            7️⃣ **Телекоммуникации и информационная безопасность**  
            Работа с сетевыми технологиями, защита данных и систем от угроз.  

            🌟 *Выберите свою дисциплину и покажите свои способности!*  
            """;

        sendMessage(chatId, text, "Markdown");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getFrom().getUserName();
                startCommand(chatId, userName);
            }
            case INFO -> infoCommand(chatId);
            case STAGES -> stagesCommand(chatId);
            case SOCIALS -> socialsCommand(chatId);
            case DISCIPLINES -> disciplinesCommand(chatId);
            case FAQ -> faqCommand(chatId);
            case HELP -> helpCommand(chatId);
            default -> {
                if (message.startsWith("/ask")) {
                    handleAskCommand(chatId, message);
                } else {
                    unknownCommand(chatId);
                }
            }
        }
    }
}
