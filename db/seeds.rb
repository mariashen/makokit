# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)


Lesson.create(name: "First lesson")

4.times do |i|

	instruction = Instruction.create(text: "Seeded instruction number #{i}", lesson_id: Lesson.first.id )
	3.times do |j|
		instruction.answers.build(text: "Answer #{j} to instruction #{instruction.id}").save
	end

end



