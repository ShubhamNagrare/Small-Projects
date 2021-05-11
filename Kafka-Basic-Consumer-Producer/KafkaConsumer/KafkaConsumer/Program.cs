using KafkaNet;
using KafkaNet.Model;
using KafkaNet.Protocol;
using System;
using System.Text;

namespace KafkaConsumer
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("I AM KAFKA CONSUMER");

            while (true)
            {
                Console.WriteLine();
                Console.WriteLine();


                string topic = "chat-message";
                Uri uri = new Uri("http://localhost:9092");
                var options = new KafkaOptions(uri);
                var router = new BrokerRouter(options);
                var consuer = new Consumer(new ConsumerOptions(topic, router));
                foreach (var message in consuer.Consume())
                {
                    Console.WriteLine("Message Received : " + Encoding.UTF8.GetString(message.Value));
                }
                Console.Read();

            }
        }
    }
}
