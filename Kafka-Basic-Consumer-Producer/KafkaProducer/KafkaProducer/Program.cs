using System;
using System.Collections.Generic;
using Confluent.Kafka;
using KafkaNet;
using KafkaNet.Model;
using KafkaNet.Protocol;

namespace KafkaProducer
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("I AM KAFKA PRODUCER");

            while (true)
            {
                Console.WriteLine();
                Console.WriteLine();
                Console.WriteLine("Enter Message to Send =>");

                string msg = Console.ReadLine();
                if(msg == "exit")
                {
                    break;
                }


                string topic = "chat-message";
                Message msgg = new Message(msg);
                Uri uri = new Uri("http://localhost:9092");
                var options = new KafkaOptions(uri);
                var router = new BrokerRouter(options);
                var client = new Producer(router);
                client.SendMessageAsync(topic, new List<Message> { msgg }).Wait();
                Console.ReadLine();
            }

        }
    }
}
