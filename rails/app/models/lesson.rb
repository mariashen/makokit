class Lesson < ActiveRecord::Base

	has_many :instructions do 
		def last_index
			return (self.count - 1)
		end

		def reorder_indices(start_index = 0)
			i = start_index
			puts self.inspect
			self.each do |inst|
				inst.update_attribute(:display_index, i) # reorders everything
				i += 1
			end
		end
	end

	has_many :answers, through: :instructions


end

