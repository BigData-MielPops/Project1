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
##### CleanUp(results):
        for each newKey, value in results
            emit (newKey, value)
      
## Secondo task
##### Map(key, record):
        value = (prodId, score)
        emit (userID, value)
#####
    create global OrderedMap results
##### Reduce(key, record):
        create Array[10] top10
        for each value in record:
            top10.insertInOrderByScore(value)
        results.insert(key, top10)
##### CleanUp(results):
        for each key, value in results
            emit (key, value)

## Terzo task (Opzionale)
##### Map(key, record):
        value = (userId, score)
        emit (prodID, value)
#####
    create global Map<coppia di utenti, lista di prodotti> results
##### Reduce(key, record):
        for each value in record:
            if value.score >= 4
                for each value2 in record[value:]:
                    if value2.score >= 4
                        results.insertIntelligente(value.userId, value2.userId, prodId)
##### CleanUp(results):
        for each key, value in results
            if value.lenght >= 3
            emit (key, value)
___________________________________
## Terzo task (Opzionale)
##### Map(key, record):
        value = (userId, score)
        emit (prodID, value)
##### Reduce(key, record):
        for each value in record:
            if value.score >= 4
                for each value2 in record[value:]:
                    if value2.score >= 4
                        newKey = value.userId, value2.userId
                        emit (newKey, prodId)
##### CleanUp(results):
        for each key, value in results
            emit (key, value)
            
##### Map2(key, record):
        emit (key, record)
##### Reduce2(key, record):
        count = 0;
        for each value in record:
            count++
        if count >=3
        emit (key, record)
            