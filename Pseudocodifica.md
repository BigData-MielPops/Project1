# Pseudocodifica

## Primo task
##### Map(key, record):
        newKey = month + year + prodID
        emit (newKey, score)
#####
    create global OrderedMap results
##### Reduce(key, records):
        create Array[5] top5
        month, year, prodId = key.getData()
        for each score in records:
            totalScore += score
            totalCount++
        avg = totalScore / totalCount
        newKey = month + year
        value = (avg, prodId)
        top5.insertInOrderByAvg(value)
        results.insert(newKey, top5)
##### CleanUp():
        for each newKey, value in results
            emit (newKey, value)
      
## Secondo task
##### Map(key, record):
        value = (prodId, score)
        emit (userID, value)
#####
    create global OrderedMap results
##### Reduce(key, records):
        create Array[10] top10
        for each value in records:
            top10.insertInOrderByScore(value)
        results.insert(key, top10)
##### CleanUp():
        for each key, value in results
            emit (key, value)


## Terzo task (Opzionale) - 2x MapReduces version
##### Map(key, record):
        if score >= 4:
            emit (prodID, userId)
##### Reduce(key, record):
        for i=0; i<list.length; i++:
            user1 = list[i].userId
                 for j=i; j<list.length; j++:
                    user2 = value[j].userId
                    newKey = orderCouple(user1, user2)
                    emit (newKey, prodId)
##### Map2(key, record):
        newKey = (user1, user2)
        emit (newKey, prodID)
##### Reduce2(key, records):
        if records.length >= 3
            emit (key, records.toString())
            

## Terzo task (Opzionale) - 1x MapReduce, RAM intensive
##### Map(key, record):
        if score >= 4:
            emit (prodID, userId)
#####
    create global OrderedMap<coppia di utenti, lista di prodotti> results
##### Reduce(key, records):
        for i=0; i<list.length; i++:
            user1 = list[i].userId
                 for j=i; j<list.length; j++:
                    user2 = value[j].userId
                    results.insert(value.userId + value2.userId, prodId)
##### CleanUp():
        for each key, value in results
            if value.lenght >= 3
            emit (key, value)