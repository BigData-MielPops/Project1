# Pseudocodifica
## Primo task
    create global Map scoreSum
##### Map(key, record):
        newKey = month + year + prodID
        scoreSum[newKey] = scoreSum[newKey] + score
        emit (newKey, scoreSum[newKey])
#####
    create global OrderedMap results
##### Reduce(key, record):
        create Array[5] top5
        month, year, prodId = key.getData()
        for each value in record:
            if value > maxScore
                maxScore = value
            totalCount ++
        avg = maxScore / totalCount
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
