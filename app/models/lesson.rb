class Lesson < ActiveRecord::Base

	has_many :instructions 
	has_many :answers, through: :instructions


end

