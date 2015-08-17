#!/usr/bin/env bash

should_contains() {
    SUB=$1
    if [[ $RESPONSE == *"$SUB"* ]]; then
        return 0
    else
        return 1
    fi
}

echo -e "GET list:"
RESPONSE=`curl http://localhost:8080/v0/tickets/2/messages 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"get"' && should_contains '"text":"PowerOJ"' && should_contains '"ticketId":"2"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "GET 2-1"
RESPONSE=`curl http://localhost:8080/v0/tickets/2/messages/1 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"get"' && should_contains '"author":"power"' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "PUT 2-1"
RESPONSE=`curl -X PUT http://localhost:8080/v0/tickets/2/messages/1 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"put"' && should_contains '"data":"updated"' && should_contains '"ticketId":"2"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "POST 2"
RESPONSE=`curl -H "Content-Type: application/json" -X POST -d '{"text":"ok", "author":"test"}' http://localhost:8080/v0/tickets/2/messages 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"post"' && should_contains '"author":"test"' && should_contains '"ticketId":"2"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "PATCH 2-1"
RESPONSE=`curl -X PATCH http://localhost:8080/v0/tickets/2/messages/1 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"patch"' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "DELETE 2-1"
RESPONSE=`curl -X DELETE http://localhost:8080/v0/tickets/2/messages/1 2>/dev/null`
echo "$RESPONSE"
if should_contains '"method":"delete"' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "GET status 2-1"
RESPONSE=`curl http://localhost:8080/v0/tickets/2/messages/1/status 2>/dev/null`
echo "$RESPONSE"
if should_contains '"status":true' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "POST status 2-1"
RESPONSE=`curl -X POST http://localhost:8080/v0/tickets/2/messages/1/status 2>/dev/null`
echo "$RESPONSE"
if should_contains '"created":true' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "PUT status 2-1"
RESPONSE=`curl -X PUT http://localhost:8080/v0/tickets/2/messages/1/status 2>/dev/null`
echo "$RESPONSE"
if should_contains '"updated":true' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "PATCH status 2-1"
RESPONSE=`curl -X PATCH http://localhost:8080/v0/tickets/2/messages/1/status 2>/dev/null`
echo "$RESPONSE"
if should_contains '"patched":true' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

echo -e "DELETE status 2-1"
RESPONSE=`curl -X DELETE http://localhost:8080/v0/tickets/2/messages/1/status 2>/dev/null`
echo "$RESPONSE"
if should_contains '"deleted":true' && should_contains '"ticketId":"2"' && should_contains '"messageId":"1"'; then
    echo -e "\x1b[32mPASS\x1b[0m"
else
    echo -e "\x1b[31mFAIL\x1b[0m"
fi

exit 0

#curl -v -H "Content-Type: application/json" -X POST --data "@message.json" http://localhost:8080/v0/tickets/2/messages
# sudo tcpdump -A -s 0 -q -t -i lo 'port 8080'

#var script = document.createElement('script');
#script.type = 'text/javascript';
#script.src = 'http://www.oschina.net/js/2012/jquery-1.7.1.min.js?t=1399607778000';
#document.head.appendChild(script);
#
#$.post("/v0/tickets/2/messages",{id:"5"},function(data){});
