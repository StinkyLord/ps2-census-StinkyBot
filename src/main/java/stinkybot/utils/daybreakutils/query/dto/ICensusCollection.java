package stinkybot.utils.daybreakutils.query.dto;

import stinkybot.utils.daybreakutils.anatomy.Collection;
import stinkybot.utils.daybreakutils.tree.Pair;
import stinkybot.utils.daybreakutils.tree.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public interface ICensusCollection {

	public Collection getCollection();

	public List<ICensusCollection> getNested();

	public void parse(JsonNode json, TreeNode<Pair<Collection, String>> resolveTree, boolean fromTree) throws IllegalArgumentException, IOException;
}
